package ru.rustam.otus.rabbitmq.service;

import ru.rustam.otus.rabbitmq.model.ClientMessage;
import ru.rustam.otus.rabbitmq.model.DeliveryCompletedMessage;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.rabbitmq.model.PaymentCompletedMessage;
import ru.rustam.otus.rabbitmq.model.PaymentResultMessage;

public interface RabbitService {

    void sendFailMessage(FailMessage message);

    void sendPaymentCreatedMessage(OrderMessage message);

    void sendOrderCreatedMessage(OrderMessage message);

    void sendOrderReservedMessage(OrderMessage message);

    void sendPaymentCompletedMessage(PaymentCompletedMessage message);

    void deliveryCompletedMessage(DeliveryCompletedMessage message);

    void sendClientMessage(ClientMessage message);

    void sendPaymentResultMessage(PaymentResultMessage message);
}
