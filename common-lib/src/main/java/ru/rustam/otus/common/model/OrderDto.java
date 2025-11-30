package ru.rustam.otus.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rustam.otus.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private OffsetDateTime created;

    @JsonIgnore
    public String getPrintableStatus() {
        return switch (OrderStatus.valueOf(status)) {
            case OrderStatus.CREATED -> "Создан";
            case OrderStatus.CANCELED -> "Отменён";
            case OrderStatus.COMPLETED -> "Завершён";
            case OrderStatus.AWAIT_PAYMENT -> "Ожидает оплаты";
            case OrderStatus.AWAIT_DELIVERY -> "Ожидает доставки";
            default -> status;
        };
    }
}
