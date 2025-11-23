package ru.rustam.otus.fbbe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rustam.otus.fbbe.model.Order;
import ru.rustam.otus.fbbe.model.OrderItem;
import ru.rustam.otus.fbbe.service.CartService;
import ru.rustam.otus.fbbe.service.OrderService;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value("${orderServiceHost}")
    private String ORDER_SERVICE_URL;
    private final RestTemplate restTemplate;
    private final CartService cartService;

    @Override
    public List<Order> getAllClientOrders(String userName) {
        ParameterizedTypeReference<List<Order>> typeRef =
                new ParameterizedTypeReference<>() {
                };
        return restTemplate.exchange(ORDER_SERVICE_URL + "/orders/{userName}",
                HttpMethod.GET, HttpEntity.EMPTY, typeRef, Map.of("userName", userName)).getBody();
    }

    @Override
    public void createOrder(String orderId, String userName) {
        var cart = cartService.getCart(userName);
        DefaultOidcUser user = ((DefaultOidcUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal());
        var items = cart.getProducts().stream()
                .map(cp -> new OrderItem(cp.getProductId(), cp.getCount()))
                .toList();
        var orderToSend = Order.builder()
                .orderId(orderId)
                .userName(userName)
                .contactPhone(user.getAttribute("contact_phone"))
                .deliveryAddress(user.getAttribute("delivery_address"))
                .email(user.getEmail())
                .amount(cart.calculateAmount())
                .items(items)
                .build();
        var res = restTemplate.postForObject(ORDER_SERVICE_URL + "/order", orderToSend, Order.class);
        log.debug("Response: {}", res);
        cartService.clearCart(userName);
    }


}
