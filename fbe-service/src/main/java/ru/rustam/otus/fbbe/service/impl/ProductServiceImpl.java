package ru.rustam.otus.fbbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rustam.otus.fbbe.model.Product;
import ru.rustam.otus.fbbe.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final List<Product> products = List.of(
            new Product(1L, "CAT", BigDecimal.valueOf(200), "/images/cat.jpeg"),
            new Product(2L, "DOG", BigDecimal.valueOf(500), "/images/cat.jpeg"),
            new Product(3L, "Сковородка алюминиевая, 26см", BigDecimal.valueOf(500), "/images/cat.jpeg")
    );

    @Override
    public List<Product> getAllProducts() {
        return products;
    }

    @Override
    public Optional<Product> getProductById(long productId) {
        for (Product product : products) {
            if (productId == product.getId()) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

}
