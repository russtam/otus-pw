package ru.rustam.otus.storage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.rustam.otus.storage.ConvertUtil;
import ru.rustam.otus.storage.model.StorageItemDto;
import ru.rustam.otus.storage.service.StorageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/products")
    public List<StorageItemDto> createOrder() {
        log.debug("/products");
        var products = storageService.getProducts();
        return ConvertUtil.convertStorageList(products);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StorageItemDto> getOrder(@PathVariable long productId) {
        var orderDto = storageService.getProduct(productId);
        return ResponseEntity.ok(ConvertUtil.convertStorageItem(orderDto));
    }

}
