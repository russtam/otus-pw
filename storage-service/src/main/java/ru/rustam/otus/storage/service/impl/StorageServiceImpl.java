package ru.rustam.otus.storage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rustam.otus.rabbitmq.model.OrderItemMessage;
import ru.rustam.otus.rabbitmq.model.OrderMessage;
import ru.rustam.otus.rabbitmq.service.MessageService;
import ru.rustam.otus.storage.db.StorageItemEntity;
import ru.rustam.otus.storage.db.StorageItemRepository;
import ru.rustam.otus.storage.service.StorageService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageItemRepository storageItemRepository;
    private final MessageService messageService;

    @Override
    @Transactional
    public void reserveItemsForOrder(OrderMessage message) {
        List<Long> ids = message.getItems().stream().map(OrderItemMessage::getItemId).toList();
        List<StorageItemEntity> foundList = storageItemRepository.findAllById(ids);
        if (foundList.size() != ids.size()) {
            throw new IllegalArgumentException("Wring items ids");
        }
        for (StorageItemEntity entity : foundList) {
            var msgItemOpt = message.getItems().stream()
                    .filter(oi -> oi.getItemId() == entity.getItemId()).findFirst();
            var msgItem = msgItemOpt.orElseThrow(() -> new RuntimeException("Item id not found"));
            if (msgItem.getCount() <= entity.getAvailable()) {
                entity.setAvailable(entity.getAvailable() - msgItem.getCount());
                entity.setReserved(entity.getReserved() + msgItem.getCount());
            } else {
                throw new RuntimeException("Not enough items in storage");
            }
        }
        storageItemRepository.saveAll(foundList);
        //Зарезервировали, шлём дальше на оплату
        messageService.sendOrderReservedMessage(message);
    }

    @Override
    @Transactional
    public void unreserveItems(OrderMessage message) {
        List<Long> ids = message.getItems().stream().map(OrderItemMessage::getItemId).toList();
        List<StorageItemEntity> foundList = storageItemRepository.findAllById(ids);
        if (foundList.size() != ids.size()) {
            throw new IllegalArgumentException("Wring items ids");
        }
        for (StorageItemEntity entity : foundList) {
            var msgItemOpt = message.getItems().stream()
                    .filter(oi -> oi.getItemId() == entity.getItemId()).findFirst();
            var msgItem = msgItemOpt.orElseThrow(() -> new RuntimeException("Item id not found"));
            entity.setAvailable(entity.getAvailable() + msgItem.getCount());
            entity.setReserved(entity.getReserved() - msgItem.getCount());
        }
        storageItemRepository.saveAll(foundList);
    }

}
