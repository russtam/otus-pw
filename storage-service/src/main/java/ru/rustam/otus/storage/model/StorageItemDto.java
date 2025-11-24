package ru.rustam.otus.storage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageItemDto {

    private Long itemId;

    private String name;

    private BigDecimal price;

    private String imageUrl;

    private int available;
    private int reserved;

}
