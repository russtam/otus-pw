package ru.rustam.otus.fbbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @JsonProperty("itemId")
    private long id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private int available;
    private int reserved;
}
