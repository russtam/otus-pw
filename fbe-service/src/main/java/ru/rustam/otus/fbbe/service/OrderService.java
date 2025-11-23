package ru.rustam.otus.fbbe.service;

import ru.rustam.otus.fbbe.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllClientOrders(String username);

    void createOrder(String orderId, String userName);

}
