package ru.rustam.otus.order.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {

    private String orderId;

    private String userName;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @NotEmpty
    private List<OrderItemDto> items;

    @NotNull
    private String deliveryAddress;

    @NotNull
    private String contactPhone;

    private String email;

    private String status;

    private String paymentLink;
}
