package ru.rustam.otus.storage.db;

import jakarta.persistence.Column;
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

@Entity
@Table(name = "storage_tbl")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StorageItemEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String name;

    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    private int available;
    private int reserved;

    public StorageItemEntity(String name, int available, int reserved) {
        this.name = name;
        this.available = available;
        this.reserved = reserved;
    }



    public StorageItemEntity(String name,
                             BigDecimal price,
                             int available,
                             int reserved,
                             String imageUrl) {
        this.name = name;
        this.price = price;
        this.available = available;
        this.reserved = reserved;
        this.imageUrl = imageUrl;
    }

}
