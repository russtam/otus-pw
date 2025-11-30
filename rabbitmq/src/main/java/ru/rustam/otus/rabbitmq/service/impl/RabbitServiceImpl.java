package ru.rustam.otus.rabbitmq.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.rustam.otus.rabbitmq.model.ClientMessage;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.PaymentCreatedMessage;
import ru.rustam.otus.rabbitmq.model.PaymentMessage;
import ru.rustam.otus.rabbitmq.model.PaymentResultMessage;
import ru.rustam.otus.rabbitmq.model.SimpleMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.CLIENT_MESSAGE_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.DELIVERY_COMPLETED_FANOUT_EXCHANGE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.DELIVERY_STARTED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.FAIL_FANOUT_EXCHANGE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_CREATED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_RESERVED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_COMPLETED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_CREATED_FANOUT_EXCHANGE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_RESULT_QUEUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitServiceImpl implements RabbitService {

    private static final String SENT_LOG = "Sent to {}: {}";
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendFailMessage(FailMessage message) {
        //Отправляем в Fanout exchange, кому надо прибиндятся к нему
        rabbitTemplate.convertAndSend(FAIL_FANOUT_EXCHANGE, "", message);
        log.debug(SENT_LOG, FAIL_FANOUT_EXCHANGE, message);
    }

    @Override
    public void sendPaymentCreatedMessage(PaymentCreatedMessage message) {
        //Отправляем в Fanout exchange, кому надо прибиндятся к нему
        rabbitTemplate.convertAndSend(PAYMENT_CREATED_FANOUT_EXCHANGE, "", message);
        log.debug(SENT_LOG, PAYMENT_CREATED_FANOUT_EXCHANGE, message);
    }

    @Override
    public void sendOrderCreatedMessage(SimpleMessage message) {
        rabbitTemplate.convertAndSend(ORDER_CREATED_QUEUE, message);
        log.debug(SENT_LOG, ORDER_CREATED_QUEUE, message);
    }

    @Override
    public void sendOrderReservedMessage(SimpleMessage message) {
        rabbitTemplate.convertAndSend(ORDER_RESERVED_QUEUE, message);
        log.debug(SENT_LOG, ORDER_RESERVED_QUEUE, message);
    }

    @Override
    public void sendPaymentCompletedMessage(PaymentMessage message) {
        rabbitTemplate.convertAndSend(PAYMENT_COMPLETED_QUEUE, message);
        log.debug(SENT_LOG, PAYMENT_COMPLETED_QUEUE, message);
    }

    @Override
    public void deliveryCompletedMessage(SimpleMessage message) {
        rabbitTemplate.convertAndSend(DELIVERY_COMPLETED_FANOUT_EXCHANGE, "", message);
        log.debug(SENT_LOG, DELIVERY_COMPLETED_FANOUT_EXCHANGE, message);
    }

    @Override
    public void deliveryStartedMessage(SimpleMessage message) {
        rabbitTemplate.convertAndSend(DELIVERY_STARTED_QUEUE, message);
        log.debug(SENT_LOG, DELIVERY_STARTED_QUEUE, message);
    }

    @Override
    public void sendClientMessage(ClientMessage message) {
        rabbitTemplate.convertAndSend(CLIENT_MESSAGE_QUEUE, message);
        log.debug(SENT_LOG, CLIENT_MESSAGE_QUEUE, message);
    }

    @Override
    public void sendPaymentResultMessage(PaymentResultMessage message) {
        rabbitTemplate.convertAndSend(PAYMENT_RESULT_QUEUE, message);
        log.debug(SENT_LOG, PAYMENT_RESULT_QUEUE, message);
    }

}
