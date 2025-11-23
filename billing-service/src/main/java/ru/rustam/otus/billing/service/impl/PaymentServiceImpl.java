package ru.rustam.otus.billing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rustam.otus.billing.db.PaymentEntity;
import ru.rustam.otus.billing.db.PaymentRepository;
import ru.rustam.otus.billing.exception.PaymentFailedException;
import ru.rustam.otus.billing.service.PaymentService;
import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.rabbitmq.service.MessageService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MessageService messageService;

    @Override
    public void makePayment(OrderMessage message) {
        //при сумме больше 100 тыс. будем эмулировать ошибку платежа, чтобы можно было
        //посмотреть откат и неуспешный заказ
        if (message.getAmount().compareTo(BigDecimal.valueOf(100_000)) > 0) {
            throw new PaymentFailedException("Amount is too large");
        }
        //Платёж будем симулировать - просто сохраним успешный платёж в БД
        paymentRepository.save(PaymentEntity.builder()
                .orderId(message.getOrderId())
                .paymentDate(OffsetDateTime.now())
                .amount(message.getAmount())
                .status("SUCCESS")
                .build());
        messageService.sendPaymentCompletedMessage(message);
    }

}
