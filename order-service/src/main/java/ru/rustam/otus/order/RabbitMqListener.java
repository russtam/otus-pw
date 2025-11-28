package ru.rustam.otus.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.order.db.OrderRepository;
import ru.rustam.otus.order.exceptions.OrderException;
import ru.rustam.otus.order.service.OrderService;
import ru.rustam.otus.rabbitmq.model.DeliveryCompletedMessage;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;

import static ru.rustam.otus.order.configuration.RabbitConfiguration.FAIL_QUEUE;
import static ru.rustam.otus.order.configuration.RabbitConfiguration.PAYMENT_CREATED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.DELIVERY_COMPLETED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    @RabbitListener(queues = DELIVERY_COMPLETED_QUEUE)
    public void completeListener(DeliveryCompletedMessage message) {
        try {
            log.debug("Received: {}", message);
            orderService.updateOrderStatus(message.getOrderId(), "COMPLETED");
            log.info("Order with id={} is completed", message.getOrderId());
        } catch (OrderException e) {
            log.warn("Order with id={} not found, can't set status COMPLETED", message.getOrderId());
        }
    }

    @RabbitListener(queues = PAYMENT_CREATED_QUEUE)
    public void paymentCreatedListener(OrderMessage message) {
        try {
            log.debug("Received: {}", message);
            var order = orderService.getOrder(message.getOrderId());
            if (!"CREATED".equals(order.getStatus())) {
                throw new OrderException("Wrong order status: " + order.getStatus());
            }
            order.setStatus("AWAIT_PAYMENT");
            order.setPaymentLink(message.getPaymentLink());
            orderService.saveOrder(order);
            log.info("Order with id={} is await payment", message.getOrderId());
        } catch (OrderException e) {
            log.warn("Order with id={} not found, can't set status AWAIT_PAYMENT", message.getOrderId());
        }
    }

    @RabbitListener(queues = FAIL_QUEUE)
    public void failListener(FailMessage message) {
        try {
            log.debug("Received: {}", message);
            orderService.updateOrderStatus(message.getOrderId(), "CANCELED");
            log.info("Order with id={} is canceled", message.getOrderId());
        } catch (OrderException e) {
            log.warn("Order with id={} not found, can't set status CANCELED", message.getOrderId());
        }
    }

}
