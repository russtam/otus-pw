package ru.rustam.otus.billing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.billing.service.PaymentService;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.rabbitmq.service.MessageService;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_RESERVED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    private static final String SOURCE = "billing-service";

    private final PaymentService paymentService;
    private final MessageService messageService;

    @RabbitListener(queues = ORDER_RESERVED_QUEUE)
    public void messageListener(OrderMessage message) {
        try {
            log.debug("From {} received: {}", ORDER_RESERVED_QUEUE, message);
            paymentService.makePayment(message);
            log.info("Order with id={} is payed", message.getOrderId());
        } catch (Exception e) {
            log.error("Exception while making payment for order {}", message.getOrderId(), e);
            FailMessage failMessage = new FailMessage();
            failMessage.setOrderId(message.getOrderId());
            failMessage.setError("Payment error: " + e);
            failMessage.setOrder(message);
            failMessage.setSource(SOURCE);
            messageService.sendFailMessage(failMessage);
        }
    }

}
