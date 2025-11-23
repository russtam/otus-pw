package ru.rustam.otus.order.service.impl;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rustam.otus.order.db.OrderEntity;
import ru.rustam.otus.order.db.OrderRepository;
import ru.rustam.otus.order.exceptions.OrderNotFoundException;
import ru.rustam.otus.order.service.OrderService;
import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.rabbitmq.service.MessageService;

import java.util.List;

import static ru.rustam.otus.order.ConvertUtil.convertItemListForMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MessageService messageService;

    @Override
    public OrderEntity createOrder(OrderEntity order) {
        if (StringUtils.isBlank(order.getStatus())) {
            order.setStatus("CREATED");
        }
        var createdOrder = orderRepository.save(order);
        messageService.sendOrderCreatedMessage(OrderMessage.builder()
                .amount(order.getAmount())
                .orderId(createdOrder.getOrderId())
                .contactPhone(order.getContactPhone())
                .deliveryAddress(order.getDeliveryAddress())
                .status(order.getStatus())
                .items(convertItemListForMessage(order.getItems()))
                .build());
        return createdOrder;
    }

    @Override
    public OrderEntity getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Override
    public List<OrderEntity> getOrders(String userName) {
        return orderRepository.findAllByUserName(userName);
    }

    @Override
    public void deleteOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void updateOrder(String orderId, OrderEntity order) {
        order.setOrderId(orderId);
        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(String orderId, String newStatus) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

}
