package ru.rustam.otus.billing.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rustam.otus.billing.service.PaymentService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BillingController {

    private final PaymentService paymentService;

    /**
     * Ручка для внешнего сервиса, чтобы сообщить результат платежа.
     * Можно заменить на очередь.
     *
     * @param paymentId идентификатор платежа
     * @param success   true если успех
     */
    @PostMapping("/payment")
    public void updatePayment(@RequestParam String paymentId,
                              @RequestParam boolean success) {
        log.debug("updatePayment, paymentId={}, success={}", paymentId, success);
        paymentService.updatePayment(paymentId, success);
    }

}
