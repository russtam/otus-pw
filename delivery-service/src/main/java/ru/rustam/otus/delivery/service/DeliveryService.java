package ru.rustam.otus.delivery.service;

import ru.rustam.otus.rabbitmq.model.OrderMessage;

public interface DeliveryService {

    void startDelivery(OrderMessage message);

}
