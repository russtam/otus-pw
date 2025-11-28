package ru.rustam.otus.order.db;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders_tbl")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderEntity {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "user_name")
    private String userName;

    private BigDecimal amount;

    @Convert(converter = OrderItemsConverter.class)
    private List<OrderItem> items;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "contact_phone")
    private String contactPhone;

    private String email;

    private String status;

    private String paymentLink;

}
