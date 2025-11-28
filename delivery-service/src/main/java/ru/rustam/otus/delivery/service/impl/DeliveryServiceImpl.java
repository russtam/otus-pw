package ru.rustam.otus.delivery.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rustam.otus.delivery.db.DeliveryEntity;
import ru.rustam.otus.delivery.db.DeliveryRepository;
import ru.rustam.otus.delivery.service.DeliveryService;
import ru.rustam.otus.rabbitmq.model.DeliveryCompletedMessage;
import ru.rustam.otus.rabbitmq.model.PaymentCompletedMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;

import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final RabbitService messageService;

    @Override
    public void startDelivery(PaymentCompletedMessage message) {
        //Доставку просто сохраним в БД
        deliveryRepository.save(DeliveryEntity.builder()
                .deliveryDate(OffsetDateTime.now())
                .orderId(message.getOrderId())
                .status("READY")
                .deliveryAddress("Some address")
                .contactPhone("Contact phone")
                .build());
        messageService.deliveryCompletedMessage(new DeliveryCompletedMessage(message.getOrderId()));
    }

}
