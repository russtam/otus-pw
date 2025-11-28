package ru.rustam.otus.rabbitmq.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.rustam.otus.rabbitmq.model.ClientMessage;
import ru.rustam.otus.rabbitmq.model.DeliveryCompletedMessage;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.rabbitmq.model.PaymentCompletedMessage;
import ru.rustam.otus.rabbitmq.model.PaymentResultMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.CLIENT_MESSAGE_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.DELIVERY_COMPLETED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.FAIL_FANOUT_EXCHANGE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_CREATED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_RESERVED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_COMPLETED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_CREATED_FANOUT_EXCHANGE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_RESULT_QUEUE;

@Service
@RequiredArgsConstructor
public class RabbitServiceImpl implements RabbitService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendFailMessage(FailMessage message) {
        //Отправляем в Fanout exchange, кому надо прибиндятся к нему
        rabbitTemplate.convertAndSend(FAIL_FANOUT_EXCHANGE, "", message);
    }

    @Override
    public void sendPaymentCreatedMessage(OrderMessage message) {
        //Отправляем в Fanout exchange, кому надо прибиндятся к нему
        rabbitTemplate.convertAndSend(PAYMENT_CREATED_FANOUT_EXCHANGE, "", message);
    }

    @Override
    public void sendOrderCreatedMessage(OrderMessage message) {
        rabbitTemplate.convertAndSend(ORDER_CREATED_QUEUE, message);
    }

    @Override
    public void sendOrderReservedMessage(OrderMessage message) {
        rabbitTemplate.convertAndSend(ORDER_RESERVED_QUEUE, message);
    }

    @Override
    public void sendPaymentCompletedMessage(PaymentCompletedMessage message) {
        rabbitTemplate.convertAndSend(PAYMENT_COMPLETED_QUEUE, message);
    }

    @Override
    public void deliveryCompletedMessage(DeliveryCompletedMessage message) {
        rabbitTemplate.convertAndSend(DELIVERY_COMPLETED_QUEUE, message);
    }

    @Override
    public void sendClientMessage(ClientMessage message) {
        rabbitTemplate.convertAndSend(CLIENT_MESSAGE_QUEUE, message);
    }

    @Override
    public void sendPaymentResultMessage(PaymentResultMessage message) {
        rabbitTemplate.convertAndSend(PAYMENT_RESULT_QUEUE, message);
    }
}
