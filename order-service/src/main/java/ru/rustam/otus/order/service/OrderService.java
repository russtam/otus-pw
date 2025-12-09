package ru.rustam.otus.order.service;

import ru.rustam.otus.order.db.OrderEntity;

import java.util.List;

public interface OrderService {

    OrderEntity createOrder(OrderEntity order);

    OrderEntity getOrder(String orderId);

    List<OrderEntity> getOrders(String userName);

    void deleteOrder(String orderId);

    void updateOrder(String orderId, OrderEntity order);

    void saveOrder(OrderEntity order);

    void updateOrderStatus(String orderId, String newStatus);

}
