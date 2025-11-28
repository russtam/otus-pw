package ru.rustam.otus.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.messaging.service.ClientMessagingService;
import ru.rustam.otus.rabbitmq.model.ClientMessage;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.CLIENT_MESSAGE_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_RESERVED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    private final ClientMessagingService clientMessagingService;

    @RabbitListener(queues = CLIENT_MESSAGE_QUEUE)
    public void messageListener(ClientMessage message) {
        try {
            log.debug("From {} received: {}", CLIENT_MESSAGE_QUEUE, message);
            clientMessagingService.deliverMessage(message);
            log.info("Message for user {} is scheduled for delivery", message.getUserName());
        } catch (Exception e) {
            log.error("Exception while saving message for user {}", message.getUserName());
        }
    }

}
