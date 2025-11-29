package ru.rustam.otus.billing.service;

public interface PaymentService {

    void createPayment(String orderId);

    void updatePayment(String paymentId, boolean success);

}
