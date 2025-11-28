package ru.rustam.otus.messaging.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.rustam.otus.common.model.ClientMessageDto;
import ru.rustam.otus.messaging.service.ClientMessagingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientMessagingController {

    private final ClientMessagingService clientMessagingService;

    @GetMapping("/messages/{userName}")
    public ResponseEntity<List<ClientMessageDto>> getOrders(@PathVariable String userName) {
        var orders = clientMessagingService.getMessagesByUsername(userName);
        return ResponseEntity.ok(orders);
    }

}
