package ru.rustam.otus.storage.service;

import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.storage.db.StorageItemEntity;

import java.util.List;

public interface StorageService {

    void reserveItemsForOrder(OrderMessage message);

    void unreserveItems(OrderMessage message);

    List<StorageItemEntity> getProducts();

    StorageItemEntity getProduct(long productId);

}
