package ru.rustam.otus.delivery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.delivery.service.DeliveryService;
import ru.rustam.otus.rabbitmq.model.PaymentMessage;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_COMPLETED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    private final DeliveryService deliveryService;

    @RabbitListener(queues = PAYMENT_COMPLETED_QUEUE)
    public void messageListener(PaymentMessage message) {
        try {
            log.debug("From {} received: {}", PAYMENT_COMPLETED_QUEUE, message);
            deliveryService.startDelivery(message);
            log.info("Order with id={} is scheduled for delivery", message.getOrderId());
        } catch (Exception e) {
            log.error("Exception while saving delivery for order {}", message.getOrderId());
        }
    }

}
