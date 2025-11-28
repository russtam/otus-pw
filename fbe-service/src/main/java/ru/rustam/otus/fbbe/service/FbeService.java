package ru.rustam.otus.fbbe.service;

import ru.rustam.otus.common.model.OrderDto;

import java.util.List;

public interface FbeService {

    List<OrderDto> getAllClientOrders(String username);

    void createOrder(String orderId, String userName);

}
