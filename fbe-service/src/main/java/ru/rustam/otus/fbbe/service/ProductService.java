package ru.rustam.otus.fbbe.service;

import ru.rustam.otus.fbbe.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(long productId);

}
