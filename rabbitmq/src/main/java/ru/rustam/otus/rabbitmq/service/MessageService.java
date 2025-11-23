package ru.rustam.otus.rabbitmq.service;

import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;

public interface MessageService {

    void sendFailMessage(FailMessage message);

    void sendOrderCreatedMessage(OrderMessage message);

    void sendOrderReservedMessage(OrderMessage message);

    void sendPaymentCompletedMessage(OrderMessage message);

    void completedMessage(OrderMessage message);
}
