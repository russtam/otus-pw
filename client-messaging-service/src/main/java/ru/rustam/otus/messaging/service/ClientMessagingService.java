package ru.rustam.otus.messaging.service;

import ru.rustam.otus.common.model.ClientMessageDto;
import ru.rustam.otus.rabbitmq.model.ClientMessage;

import java.util.List;

public interface ClientMessagingService {

    void deliverMessage(ClientMessage message);

    List<ClientMessageDto> getMessagesByUsername(String username);

}
