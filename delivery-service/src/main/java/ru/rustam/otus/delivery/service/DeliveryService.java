package ru.rustam.otus.delivery.service;

import ru.rustam.otus.rabbitmq.model.PaymentMessage;

public interface DeliveryService {

    void startDelivery(PaymentMessage message);

}
