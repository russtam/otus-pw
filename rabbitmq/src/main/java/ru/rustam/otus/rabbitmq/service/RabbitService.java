package ru.rustam.otus.rabbitmq.service;

import ru.rustam.otus.rabbitmq.model.ClientMessage;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.PaymentCreatedMessage;
import ru.rustam.otus.rabbitmq.model.PaymentMessage;
import ru.rustam.otus.rabbitmq.model.PaymentResultMessage;
import ru.rustam.otus.rabbitmq.model.SimpleMessage;

public interface RabbitService {

    void sendFailMessage(FailMessage message);

    void sendPaymentCreatedMessage(PaymentCreatedMessage message);

    void sendOrderCreatedMessage(SimpleMessage message);

    void sendOrderReservedMessage(SimpleMessage message);

    void sendPaymentCompletedMessage(PaymentMessage message);

    void deliveryCompletedMessage(SimpleMessage message);

    void sendClientMessage(ClientMessage message);

    void sendPaymentResultMessage(PaymentResultMessage message);

}
