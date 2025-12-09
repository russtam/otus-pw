package ru.rustam.otus.common.service;

import ru.rustam.otus.common.model.ClientMessageDto;

import java.util.List;

public interface ClientMessageService {

    List<ClientMessageDto> getAllClientMessages(String username);

}
