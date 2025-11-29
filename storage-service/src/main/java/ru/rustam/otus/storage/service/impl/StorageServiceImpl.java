package ru.rustam.otus.storage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rustam.otus.common.model.OrderItemDto;
import ru.rustam.otus.common.service.OrderClientService;
import ru.rustam.otus.rabbitmq.model.SimpleMessage;
import ru.rustam.otus.rabbitmq.service.RabbitService;
import ru.rustam.otus.storage.db.StorageItemEntity;
import ru.rustam.otus.storage.db.StorageItemRepository;
import ru.rustam.otus.storage.exceptions.StorageException;
import ru.rustam.otus.storage.service.StorageService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageItemRepository storageItemRepository;
    private final RabbitService rabbitService;
    private final OrderClientService orderClientService;

    @Override
    @Transactional
    public void reserveItemsForOrder(String orderId) {
        var order = orderClientService.getOrder(orderId);
        List<Long> ids = order.getItems().stream().map(OrderItemDto::getItemId).toList();
        List<StorageItemEntity> foundList = storageItemRepository.findAllById(ids);
        if (foundList.size() != ids.size()) {
            throw new IllegalArgumentException("Wrong items ids");
        }
        for (StorageItemEntity entity : foundList) {
            var msgItemOpt = order.getItems().stream()
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
        rabbitService.sendOrderReservedMessage(new SimpleMessage(orderId));
    }

    @Override
    @Transactional
    public void unreserveItems(String orderId) {
        var order = orderClientService.getOrder(orderId);
        List<Long> ids = order.getItems().stream().map(OrderItemDto::getItemId).toList();
        List<StorageItemEntity> foundList = storageItemRepository.findAllById(ids);
        if (foundList.size() != ids.size()) {
            throw new IllegalArgumentException("Wrong items ids");
        }
        for (StorageItemEntity entity : foundList) {
            var msgItemOpt = order.getItems().stream()
                    .filter(oi -> oi.getItemId() == entity.getItemId()).findFirst();
            var msgItem = msgItemOpt.orElseThrow(() -> new RuntimeException("Item id not found"));
            entity.setAvailable(entity.getAvailable() + msgItem.getCount());
            entity.setReserved(entity.getReserved() - msgItem.getCount());
        }
        storageItemRepository.saveAll(foundList);
    }

    @Override
    @Transactional
    public void buyoutItem(String orderId) {
        var order = orderClientService.getOrder(orderId);
        List<Long> ids = order.getItems().stream().map(OrderItemDto::getItemId).toList();
        List<StorageItemEntity> foundList = storageItemRepository.findAllById(ids);
        if (foundList.size() != ids.size()) {
            throw new IllegalArgumentException("Wrong items ids");
        }
        for (StorageItemEntity entity : foundList) {
            var msgItemOpt = order.getItems().stream()
                    .filter(oi -> oi.getItemId() == entity.getItemId()).findFirst();
            var msgItem = msgItemOpt.orElseThrow(() -> new RuntimeException("Item id not found"));
            entity.setReserved(entity.getReserved() - msgItem.getCount());
        }
        storageItemRepository.saveAll(foundList);
    }

    @Override
    public List<StorageItemEntity> getProducts() {
        return storageItemRepository.findAll();
    }

    @Override
    public StorageItemEntity getProduct(long productId) {
        return storageItemRepository
                .findById(productId)
                .orElseThrow(() -> new StorageException("Not found"));
    }

}
