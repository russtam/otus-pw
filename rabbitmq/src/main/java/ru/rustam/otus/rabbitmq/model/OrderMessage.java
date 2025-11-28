package ru.rustam.otus.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Передаём сразу всю информацию в сообщении, чтобы потом не дёргать микросервис лишний раз.
 * Ну и чтобы кода в ДЗ поменьше было.
 * Событие будет определяться очередью.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMessage {

    private String orderId;
    private String userName;
    private BigDecimal amount;
    private List<OrderItemMessage> items;
    private String deliveryAddress;
    private String contactPhone;
    private String email;
    private String status;
    private String paymentLink;

}
