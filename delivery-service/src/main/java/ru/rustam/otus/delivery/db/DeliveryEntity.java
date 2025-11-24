package ru.rustam.otus.delivery.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Entity
@Table(name = "payments_tbl")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DeliveryEntity {

    @Id
    @Column(name = "delivery_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    private String orderId;

    @Column(name = "delivery_date")
    private OffsetDateTime deliveryDate;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "contact_phone")
    private String contactPhone;

    private String status;

}
