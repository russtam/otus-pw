package ru.rustam.otus.billing.service;

import ru.rustam.otus.rabbitmq.model.OrderMessage;

public interface PaymentService {

    void makePayment(OrderMessage message);

}
