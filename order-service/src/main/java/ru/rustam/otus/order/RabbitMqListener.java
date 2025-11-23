package ru.rustam.otus.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.order.exceptions.OrderNotFoundException;
import ru.rustam.otus.order.service.OrderService;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;

import static ru.rustam.otus.order.configuration.RabbitConfiguration.FAIL_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.COMPLETED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    private final OrderService orderService;

    @RabbitListener(queues = COMPLETED_QUEUE)
    public void completeListener(OrderMessage message) {
        try {
            log.debug("Received: {}", message);
            orderService.updateOrderStatus(message.getOrderId(), "COMPLETED");
            log.info("Order with id={} is completed", message.getOrderId());
        } catch (OrderNotFoundException e) {
            log.warn("Order with id={} not found, can't set status COMPLETED", message.getOrderId());
        }
    }

    @RabbitListener(queues = FAIL_QUEUE)
    public void failListener(FailMessage message) {
        try {
            log.debug("Received: {}", message);
            orderService.updateOrderStatus(message.getOrderId(), "CANCELED");
            log.info("Order with id={} is canceled", message.getOrderId());
        } catch (OrderNotFoundException e) {
            log.warn("Order with id={} not found, can't set status CANCELED", message.getOrderId());
        }
    }

}
