package ru.rustam.otus.rabbitmq.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.rabbitmq.service.MessageService;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.COMPLETED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.FAIL_FANOUT_EXCHANGE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_CREATED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_RESERVED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_COMPLETED_QUEUE;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendFailMessage(FailMessage message) {
        //Отправляем в Fanout exchange, кому надо прибиндятся к нему
        rabbitTemplate.convertAndSend(FAIL_FANOUT_EXCHANGE, "", message);
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
    public void sendPaymentCompletedMessage(OrderMessage message) {
        rabbitTemplate.convertAndSend(PAYMENT_COMPLETED_QUEUE, message);
    }

    @Override
    public void completedMessage(OrderMessage message) {
        rabbitTemplate.convertAndSend(COMPLETED_QUEUE, message);
    }

}
