package ru.rustam.otus.delivery.service;

import ru.rustam.otus.rabbitmq.model.PaymentCompletedMessage;

public interface DeliveryService {

    void startDelivery(PaymentCompletedMessage message);

}
