package ru.rustam.otus.storage.service;

import ru.rustam.otus.storage.db.StorageItemEntity;

import java.util.List;

public interface StorageService {

    void reserveItemsForOrder(String orderId);

    void unreserveItems(String orderId);

    void buyoutItem(String orderId);

    List<StorageItemEntity> getProducts();

    StorageItemEntity getProduct(long productId);

}
