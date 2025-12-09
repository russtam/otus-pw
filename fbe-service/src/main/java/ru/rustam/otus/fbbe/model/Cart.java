package ru.rustam.otus.fbbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private List<CartProduct> products;

    @JsonIgnore
    public BigDecimal calculateAmount() {
        if (CollectionUtils.isEmpty(products)) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalSum = BigDecimal.ZERO;
        for (CartProduct product : products) {
            totalSum =
                    totalSum.add(product.getPrice().multiply(BigDecimal.valueOf(product.getCount())));
        }
        return totalSum;
    }

}
