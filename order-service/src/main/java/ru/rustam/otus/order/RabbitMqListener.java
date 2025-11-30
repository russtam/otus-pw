package ru.rustam.otus.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.common.enums.OrderStatus;
import ru.rustam.otus.order.exceptions.OrderException;
import ru.rustam.otus.order.service.OrderService;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.PaymentCreatedMessage;
import ru.rustam.otus.rabbitmq.model.SimpleMessage;

import static ru.rustam.otus.order.configuration.RabbitConfiguration.DELIVERY_COMPLETED_QUEUE;
import static ru.rustam.otus.order.configuration.RabbitConfiguration.FAIL_QUEUE;
import static ru.rustam.otus.order.configuration.RabbitConfiguration.PAYMENT_CREATED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.DELIVERY_STARTED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    private static final String RECEIVED_LOG = "Received from {}: {}";

    private final OrderService orderService;

    @RabbitListener(queues = DELIVERY_COMPLETED_QUEUE)
    public void deliveryCompleteListener(SimpleMessage message) {
        try {
            log.debug(RECEIVED_LOG, DELIVERY_COMPLETED_QUEUE, message);
            orderService.updateOrderStatus(message.getOrderId(), OrderStatus.COMPLETED.name());
            log.info("Order with id={} is completed", message.getOrderId());
        } catch (OrderException e) {
            log.warn("Order with id={} not found, can't set status COMPLETED", message.getOrderId());
        }
    }

    @RabbitListener(queues = DELIVERY_STARTED_QUEUE)
    public void deliveryStartedListener(SimpleMessage message) {
        try {
            log.debug(RECEIVED_LOG, DELIVERY_STARTED_QUEUE, message);
            orderService.updateOrderStatus(message.getOrderId(), OrderStatus.AWAIT_DELIVERY.name());
            log.info("Delivery started for order with id={}", message.getOrderId());
        } catch (OrderException e) {
            log.warn("Order with id={} not found, can't set status {}",
                    message.getOrderId(), OrderStatus.AWAIT_DELIVERY.name());
        }
    }

    @RabbitListener(queues = PAYMENT_CREATED_QUEUE)
    public void paymentCreatedListener(PaymentCreatedMessage message) {
        try {
            log.debug(RECEIVED_LOG, PAYMENT_CREATED_QUEUE, message);
            var order = orderService.getOrder(message.getOrderId());
            if (!OrderStatus.CREATED.name().equals(order.getStatus())) {
                throw new OrderException("Wrong order status: " + order.getStatus());
            }
            order.setStatus(OrderStatus.AWAIT_PAYMENT.name());
            order.setPaymentLink(message.getPaymentLink());
            orderService.saveOrder(order);
            log.info("Order with id={} is await payment", message.getOrderId());
        } catch (OrderException e) {
            log.warn("Order with id={} not found, can't set status {}",
                    message.getOrderId(), OrderStatus.AWAIT_PAYMENT.name());
        }
    }

    @RabbitListener(queues = FAIL_QUEUE)
    public void failListener(FailMessage message) {
        try {
            log.debug(RECEIVED_LOG, FAIL_QUEUE, message);
            orderService.updateOrderStatus(message.getOrderId(), OrderStatus.CANCELED.name());
            log.info("Order with id={} is canceled", message.getOrderId());
        } catch (OrderException e) {
            log.warn("Order with id={} not found, can't set status {}",
                    message.getOrderId(), OrderStatus.CANCELED.name());
        }
    }

}
