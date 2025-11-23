package ru.rustam.otus.storage.service;

import ru.rustam.otus.rabbitmq.model.OrderMessage;

public interface StorageService {

    void reserveItemsForOrder(OrderMessage message);

    void unreserveItems(OrderMessage message);

}
