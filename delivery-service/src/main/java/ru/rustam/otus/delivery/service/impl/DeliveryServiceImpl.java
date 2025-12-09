package ru.rustam.otus.delivery.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rustam.otus.delivery.db.DeliveryEntity;
import ru.rustam.otus.delivery.db.DeliveryRepository;
import ru.rustam.otus.delivery.service.DeliveryService;
import ru.rustam.otus.rabbitmq.model.PaymentMessage;
import ru.rustam.otus.rabbitmq.model.SimpleMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final RabbitService rabbitService;
    private ScheduledExecutorService executorService;

    @PostConstruct
    public void postConstruct() {
        executorService = new ScheduledThreadPoolExecutor(2);
    }

    @PreDestroy
    public void preDestroy() {
        executorService.shutdown();
    }

    @Override
    public void startDelivery(PaymentMessage message) {
        final var simpleMessage = new SimpleMessage(message.getOrderId());
        rabbitService.deliveryStartedMessage(simpleMessage);
        //Доставку просто сохраним в БД
        final var delivery = deliveryRepository.save(DeliveryEntity.builder()
                .deliveryDate(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.ofHours(3)))
                .orderId(message.getOrderId())
                .status("READY")
                .deliveryAddress("Some address")
                .contactPhone("Contact phone")
                .build());
        //Эмулируем что через 30 секунд доставка завершится
        executorService.schedule(() -> {
            delivery.setStatus("DELIVERED");
            rabbitService.deliveryCompletedMessage(simpleMessage);
        }, 30, TimeUnit.SECONDS);
    }

}
