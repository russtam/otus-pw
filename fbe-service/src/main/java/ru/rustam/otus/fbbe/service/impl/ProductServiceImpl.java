package ru.rustam.otus.fbbe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rustam.otus.fbbe.model.Product;
import ru.rustam.otus.fbbe.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${storageServiceHost}")
    private String STORAGE_SERVICE_URL;
    private final RestTemplate restTemplate;

    @Override
    public List<Product> getAllProducts() {
        ParameterizedTypeReference<List<Product>> typeRef =
                new ParameterizedTypeReference<>() {
                };
        return restTemplate.exchange(STORAGE_SERVICE_URL + "/products",
                HttpMethod.GET, HttpEntity.EMPTY, typeRef).getBody();
    }

    @Override
    public Optional<Product> getProductById(long productId) {
        var product = restTemplate.exchange(STORAGE_SERVICE_URL + "/product/{productId}",
                        HttpMethod.GET, HttpEntity.EMPTY, Product.class,
                        Map.of("productId", "" + productId))
                .getBody();
        return product != null ? Optional.of(product) : Optional.empty();
    }

}
