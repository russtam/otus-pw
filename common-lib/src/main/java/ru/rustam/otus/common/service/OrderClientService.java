package ru.rustam.otus.common.service;

import ru.rustam.otus.common.model.OrderDto;

import java.util.List;

public interface OrderClientService {

    OrderDto getOrder(String orderId);

    List<OrderDto> getAllClientOrders(String username);

    OrderDto createOrder(OrderDto order);

}
