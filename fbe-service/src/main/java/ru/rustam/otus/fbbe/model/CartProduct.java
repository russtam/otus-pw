package ru.rustam.otus.fbbe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {
    private long productId;
    private String name;
    private int count;
    private BigDecimal price;

    public BigDecimal getSum() {
        return price.multiply(BigDecimal.valueOf(count));
    }
}
