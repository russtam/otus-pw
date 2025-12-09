package ru.rustam.otus.fbbe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rustam.otus.fbbe.model.Cart;
import ru.rustam.otus.fbbe.model.CartProduct;
import ru.rustam.otus.fbbe.service.CartService;
import ru.rustam.otus.fbbe.service.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ProductService productService;
    private final Map<String, Cart> carts = new HashMap<>();

    @Override
    public void addToCart(String username, long productId, int count, BigDecimal price) {
        if (!carts.containsKey(username)) { // если у пользователя нет корзины - создаем новую
            carts.put(username, new Cart());
        }
        Cart cart = carts.get(username);

        List<CartProduct> products =
                cart.getProducts() != null ? cart.getProducts() : new ArrayList<>(); // получаем список элементов корзины, если они пустые - создаем новый

        Optional<CartProduct> existingProduct = products.stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst(); // ищем элемент в корзине с таким же id

        if (existingProduct.isPresent()) {
            CartProduct cartProduct = existingProduct.get();
            cartProduct.setCount(cartProduct.getCount() + count); // если элемент уже в корзине - увеличиваем количество
        } else {
            var prodOpt = productService.getProductById(productId);
            String name = prodOpt.isPresent() ? prodOpt.get().getName() : "Error";
            products.add(new CartProduct(productId, name, count, price)); // иначе добавляем новый элемент в корзину
        }

        cart.setProducts(products); //на случай если это новый список
    }

    @Override
    public Cart getCart(String userName) {
        return carts.get(userName);
    }

    @Override
    public void clearCart(String userName) {
        carts.remove(userName);
    }

}
