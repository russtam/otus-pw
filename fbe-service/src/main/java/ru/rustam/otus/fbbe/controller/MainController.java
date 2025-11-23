package ru.rustam.otus.fbbe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.rustam.otus.fbbe.model.CartProduct;
import ru.rustam.otus.fbbe.model.Product;
import ru.rustam.otus.fbbe.service.CartService;
import ru.rustam.otus.fbbe.service.OrderService;
import ru.rustam.otus.fbbe.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private static final AtomicLong COUNTER = new AtomicLong(1);
    private static final DateTimeFormatter DTF_ORDER_ID = DateTimeFormatter.ofPattern("yyDDDHHmmss");

    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping("/")
    public String root() {
        //просто редирект на index
        return "redirect:index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        log.debug("/index");
        //данные для каталога
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        log.debug("/profile called");
        DefaultOidcUser user = ((DefaultOidcUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());
        //данные для профиля
        model.addAttribute("name", user.getAttribute("given_name"));
        model.addAttribute("surname", user.getAttribute("family_name"));
        model.addAttribute("deliveryAddress", user.getAttribute("delivery_address"));
        model.addAttribute("email", user.getAttribute("email"));
        model.addAttribute("contactPhone", user.getAttribute("contact_phone"));
        model.addAttribute("accountLink", user.getAttribute("iss") + "/account");
        return "profile";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        log.debug("/cart");
        var userName = getUserName();
        var cart = cartService.getCart(userName);
        if (cart != null) {
            model.addAttribute("products", cart.getProducts());
            BigDecimal totalSum = BigDecimal.ZERO;
            for (CartProduct product : cart.getProducts()) {
                totalSum =
                        totalSum.add(product.getPrice().multiply(BigDecimal.valueOf(product.getCount())));
            }
            model.addAttribute("sum", totalSum);
        } else {
            model.addAttribute("products", Collections.emptyList());
            model.addAttribute("sum", BigDecimal.ZERO);
        }
        return "cart";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        log.debug("/orders");
        var userName = getUserName();
        model.addAttribute("orders", orderService.getAllClientOrders(userName));
        return "orders";
    }

    @GetMapping("/createOrder")
    public String createOrder(Model model) {
        log.debug("/createOrder");
        var userName = getUserName();
        String orderId = LocalDateTime.now().format(DTF_ORDER_ID) + "-" + COUNTER.getAndIncrement();
        orderService.createOrder(orderId, userName);
        model.addAttribute("orderId", orderId);
        return "ordercreated";
    }

    @GetMapping("/api/addToCart")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToCart(@RequestParam int productId, @RequestParam BigDecimal price) {
        var userName = getUserName();
        log.debug("/api/addToCart user={}, productId={}, price={}", userName, productId, price);
        cartService.addToCart(userName, productId, 1, price);
    }

    public record OAttribute(String name, String value) {
    }

    private String getUserName() {
        DefaultOidcUser user = ((DefaultOidcUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal());
        return user.getPreferredUsername();
    }

}
