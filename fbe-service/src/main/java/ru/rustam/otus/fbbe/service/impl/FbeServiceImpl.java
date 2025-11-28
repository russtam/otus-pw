package ru.rustam.otus.fbbe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import ru.rustam.otus.common.model.OrderDto;
import ru.rustam.otus.common.model.OrderItemDto;
import ru.rustam.otus.common.service.OrderClientService;
import ru.rustam.otus.fbbe.service.CartService;
import ru.rustam.otus.fbbe.service.FbeService;
import ru.rustam.otus.rabbitmq.model.ClientMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FbeServiceImpl implements FbeService {


    private final CartService cartService;
    private final OrderClientService orderClientService;
    private final RabbitService rabbitService;

    @Override
    public List<OrderDto> getAllClientOrders(String userName) {
        return orderClientService.getAllClientOrders(userName);
    }

    @Override
    public void createOrder(String orderId, String userName) {
        var cart = cartService.getCart(userName);
        DefaultOidcUser user = ((DefaultOidcUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal());
        var items = cart.getProducts().stream()
                .map(cp -> new OrderItemDto(cp.getProductId(), cp.getCount()))
                .toList();
        var orderDto = OrderDto.builder()
                .orderId(orderId)
                .userName(userName)
                .contactPhone(user.getAttribute("contact_phone"))
                .deliveryAddress(user.getAttribute("delivery_address"))
                .email(user.getEmail())
                .amount(cart.calculateAmount())
                .items(items)
                .build();
        orderClientService.createOrder(orderDto);
        cartService.clearCart(userName);
        rabbitService.sendClientMessage(ClientMessage.builder()
                .contactPhone(orderDto.getContactPhone())
                .userName(userName)
                .email(orderDto.getEmail())
                .message("Создан заказ №" + orderId + " на сумму " + orderDto.getAmount() + " руб.")
                .build());
    }


}
