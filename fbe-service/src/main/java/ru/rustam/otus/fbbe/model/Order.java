package ru.rustam.otus.fbbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String orderId;
    private String userName;
    private BigDecimal amount;
    private List<OrderItem> items;
    private String deliveryAddress;
    private String contactPhone;
    private String email;
    private String status;

    @JsonIgnore
    public String getPrintableStatus() {
        return switch (status) {
            case "CREATED" -> "Создан";
            case "AWAIT_PAYMENT" -> "Ожидает оплаты";
            default -> status;
        };
    }
}
