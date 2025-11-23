package ru.rustam.otus.fbbe.service;

import ru.rustam.otus.fbbe.model.Cart;

import java.math.BigDecimal;

public interface CartService {

    void addToCart(String username, long productId, int count, BigDecimal price);

    Cart getCart(String userName);

    void clearCart(String userName);

}
