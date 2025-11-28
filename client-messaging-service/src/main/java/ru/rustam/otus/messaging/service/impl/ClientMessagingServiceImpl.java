package ru.rustam.otus.messaging.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.rustam.otus.common.model.ClientMessageDto;
import ru.rustam.otus.messaging.db.MessageEntity;
import ru.rustam.otus.messaging.db.MessageRepository;
import ru.rustam.otus.messaging.service.ClientMessagingService;
import ru.rustam.otus.rabbitmq.model.ClientMessage;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientMessagingServiceImpl implements ClientMessagingService {

    private final MessageRepository messageRepository;

    @Override
    public void deliverMessage(ClientMessage message) {
        //Сообщение просто сохраним в БД
        messageRepository.save(MessageEntity.builder()
                .userName(message.getUserName())
                .contactPhone(message.getContactPhone())
                .email(message.getEmail())
                .message(message.getMessage())
                .created(OffsetDateTime.now())
                .build());
    }

    @Override
    public List<ClientMessageDto> getMessagesByUsername(String username) {
        var fromBd = messageRepository.findAllByUserName(username);
        if (CollectionUtils.isEmpty(fromBd)) {
            return Collections.emptyList();
        }
        return fromBd.stream()
                .map(me -> new ClientMessageDto(me.getMessage(), me.getCreated()))
                .toList();
    }


}
