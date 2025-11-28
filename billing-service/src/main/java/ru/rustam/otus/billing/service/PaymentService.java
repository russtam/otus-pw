package ru.rustam.otus.billing.service;

import ru.rustam.otus.rabbitmq.model.OrderMessage;

public interface PaymentService {

    void createPayment(OrderMessage message);

    void updatePayment(String paymentId, boolean success);

}
