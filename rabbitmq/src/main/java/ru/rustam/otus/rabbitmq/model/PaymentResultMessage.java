package ru.rustam.otus.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResultMessage {
    private String paymentId;
    private String orderId;
    private boolean success;
}
