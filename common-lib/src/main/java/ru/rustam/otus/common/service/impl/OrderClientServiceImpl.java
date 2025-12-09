package ru.rustam.otus.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rustam.otus.common.model.OrderDto;
import ru.rustam.otus.common.service.OrderClientService;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderClientServiceImpl implements OrderClientService {

    @Value("${orderServiceHost:http://otuspw-order-service-svc:8000}")
    private String ORDER_SERVICE_URL;
    private final RestTemplate restTemplate;

    @Override
    public OrderDto getOrder(String orderId) {
        return restTemplate.exchange(ORDER_SERVICE_URL + "/order/{orderId}",
                HttpMethod.GET, HttpEntity.EMPTY, OrderDto.class, Map.of("orderId", orderId)).getBody();
    }

    @Override
    public List<OrderDto> getAllClientOrders(String username) {
        ParameterizedTypeReference<List<OrderDto>> typeRef =
                new ParameterizedTypeReference<>() {
                };
        return restTemplate.exchange(ORDER_SERVICE_URL + "/orders/{userName}",
                HttpMethod.GET, HttpEntity.EMPTY, typeRef, Map.of("userName", username)).getBody();
    }

    @Override
    public OrderDto createOrder(OrderDto order) {
        var res = restTemplate.postForObject(ORDER_SERVICE_URL + "/order", order, OrderDto.class);
        log.debug("Response: {}", res);
        return res;
    }

}
