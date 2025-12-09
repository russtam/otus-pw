package ru.rustam.otus.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.rabbitmq.model.FailMessage;
import ru.rustam.otus.rabbitmq.model.SimpleMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;
import ru.rustam.otus.storage.exceptions.StorageException;
import ru.rustam.otus.storage.service.StorageService;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_CREATED_QUEUE;
import static ru.rustam.otus.storage.configuration.RabbitConfiguration.DELIVERY_COMPLETED_QUEUE;
import static ru.rustam.otus.storage.configuration.RabbitConfiguration.FAIL_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    private static final String SOURCE = "storage-service";

    private final StorageService storageService;
    private final RabbitService rabbitService;

    @RabbitListener(queues = FAIL_QUEUE)
    public void failMessageListener(FailMessage message) {
        try {
            log.debug("From {} received: {}", FAIL_QUEUE, message);
            if (SOURCE.equals(message.getSource())) {
                log.debug("Skipped, own message");
                return;
            }
            storageService.unreserveItems(message.getOrderId());
            log.info("Order with id={} is unreserved", message.getOrderId());
        } catch (Exception e) {
            log.error("Exception while unreserving items for order {}", message.getOrderId(), e);
        }
    }

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void messageListener(SimpleMessage message) {
        try {
            log.debug("From {} received: {}", ORDER_CREATED_QUEUE, message);
            storageService.reserveItemsForOrder(message.getOrderId());
            log.info("Order with id={} is reserved", message.getOrderId());
        } catch (Exception e) {
            log.error("Exception while reserving items for order {}", message.getOrderId(), e);
            FailMessage failMessage = new FailMessage();
            failMessage.setOrderId(message.getOrderId());
            failMessage.setError("Reserve error: " + e);
            failMessage.setSource(SOURCE);
            rabbitService.sendFailMessage(failMessage);
        }
    }

    @RabbitListener(queues = DELIVERY_COMPLETED_QUEUE)
    public void completeListener(SimpleMessage message) {
        try {
            log.debug("Received from {}: {}", DELIVERY_COMPLETED_QUEUE, message);
            storageService.buyoutItem(message.getOrderId());
            log.info("Order with id={} bought out", message.getOrderId());
        } catch (StorageException e) {
            log.warn("Order with id={} not found, can't buy out", message.getOrderId());
        }
    }

}
