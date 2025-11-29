package ru.rustam.otus.billing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rustam.otus.billing.db.PaymentEntity;
import ru.rustam.otus.billing.db.PaymentRepository;
import ru.rustam.otus.billing.exception.PaymentException;
import ru.rustam.otus.billing.service.PaymentService;
import ru.rustam.otus.common.model.OrderDto;
import ru.rustam.otus.common.service.OrderClientService;
import ru.rustam.otus.rabbitmq.model.ClientMessage;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.PaymentCreatedMessage;
import ru.rustam.otus.rabbitmq.model.PaymentMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;

import java.time.OffsetDateTime;
import java.util.UUID;

import static ru.rustam.otus.billing.RabbitMqListener.SOURCE;
import static ru.rustam.otus.common.enums.OrderStatus.AWAIT_PAYMENT;
import static ru.rustam.otus.common.enums.PaymentStatus.FAILED;
import static ru.rustam.otus.common.enums.PaymentStatus.SUCCESS;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${paymentLink}")
    private String paymentLinkPrefix;

    private final PaymentRepository paymentRepository;
    private final RabbitService rabbitService;
    private final OrderClientService orderClientService;

    @Override
    public void createPayment(String orderId) {
        var order = orderClientService.getOrder(orderId);
        String paymentId = UUID.randomUUID().toString();
        paymentRepository.save(PaymentEntity.builder()
                .paymentId(paymentId)
                .orderId(orderId)
                .paymentDate(OffsetDateTime.now())
                .amount(order.getAmount())
                .status("CREATED") //статус платежа
                .build());
        String paymentLink = paymentLinkPrefix + paymentId + "&orderId=" + orderId;
        rabbitService.sendPaymentCreatedMessage(PaymentCreatedMessage.builder()
                .orderId(orderId)
                .paymentId(paymentId)
                .paymentLink(paymentLink)
                .build());
        rabbitService.sendClientMessage(ClientMessage.builder()
                .userName(order.getUserName())
                .contactPhone(order.getContactPhone())
                .email(order.getEmail())
                .message("Создан платёж " + paymentId + ". Оплатить можно по ссылке " + paymentLink)
                .build());
    }

    @Override
    public void updatePayment(String paymentId, boolean success) {
        var payOpt = paymentRepository.findById(paymentId);
        var payment = payOpt.orElseThrow(() -> new PaymentException("Payment not found"));
        OrderDto order = orderClientService.getOrder(payment.getOrderId());
        if (!AWAIT_PAYMENT.name().equals(order.getStatus())) {
            throw new PaymentException("Wrong order status:" + order.getStatus());
        }
        payment.setStatus(success ? SUCCESS.name() : FAILED.name());
        paymentRepository.save(payment);
        if (success) {
            rabbitService.sendPaymentCompletedMessage(
                    new PaymentMessage(payment.getOrderId(), paymentId));
            rabbitService.sendClientMessage(ClientMessage.builder()
                    .userName(order.getUserName())
                    .contactPhone(order.getContactPhone())
                    .email(order.getEmail())
                    .message("Платёж успешно завершен.")
                    .build());
        } else {
            FailMessage failMessage = new FailMessage();
            failMessage.setOrderId(order.getOrderId());
            failMessage.setError("Payment error");
            failMessage.setSource(SOURCE);
            rabbitService.sendFailMessage(failMessage);
            rabbitService.sendClientMessage(ClientMessage.builder()
                    .userName(order.getUserName())
                    .contactPhone(order.getContactPhone())
                    .email(order.getEmail())
                    .message("Платёж не прошёл.")
                    .build());
        }
    }

}