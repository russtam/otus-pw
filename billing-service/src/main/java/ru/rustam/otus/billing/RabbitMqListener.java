package ru.rustam.otus.billing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.billing.service.PaymentService;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.rabbitmq.model.PaymentResultMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_RESERVED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_RESULT_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    public static final String SOURCE = "billing-service";

    private final PaymentService paymentService;
    private final RabbitService rabbitService;

    @RabbitListener(queues = ORDER_RESERVED_QUEUE)
    public void messageListener(OrderMessage message) {
        try {
            log.debug("From {} received: {}", ORDER_RESERVED_QUEUE, message);
            paymentService.createPayment(message);
            log.info("Order with id={} is payed", message.getOrderId());
        } catch (Exception e) {
            log.error("Exception while making payment for order {}", message.getOrderId(), e);
            FailMessage failMessage = new FailMessage();
            failMessage.setOrderId(message.getOrderId());
            failMessage.setError("Payment error: " + e);
            failMessage.setOrder(message);
            failMessage.setSource(SOURCE);
            rabbitService.sendFailMessage(failMessage);
        }
    }

    @RabbitListener(queues = PAYMENT_RESULT_QUEUE)
    public void messageListener(PaymentResultMessage message) {
        try {
            log.debug("From {} received: {}", PAYMENT_RESULT_QUEUE, message);
            paymentService.updatePayment(message.getPaymentId(), message.isSuccess());
            log.info("Payment with id={} is {}", message.getOrderId(),
                    message.isSuccess() ? "success" : "failed");
        } catch (Exception e) {
            log.error("Exception while updating payment {}", message.getPaymentId(), e);
            FailMessage failMessage = new FailMessage();
            failMessage.setOrderId(message.getOrderId());
            failMessage.setError("Payment error: " + e);
            failMessage.setSource(SOURCE);
            rabbitService.sendFailMessage(failMessage);
        }
    }

}
