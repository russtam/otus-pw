package ru.rustam.otus.storage.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.storage.db.StorageItemEntity;
import ru.rustam.otus.storage.db.StorageItemRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitialization {

    private final StorageItemRepository storageItemRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        log.debug("Application ready");
        storageItemRepository.deleteAll();
        //инициализируем тестовыми данными
        List<StorageItemEntity> testDataList = List.of(
                new StorageItemEntity("Китайский музыкальный инструмент Эрху", BigDecimal.valueOf(5213), 10, 0, "/images/1.png"),
                new StorageItemEntity("Проектор фильмов BYINTEK 4K", BigDecimal.valueOf(12357), 20, 0, "/images/2.png"),
                new StorageItemEntity("Серьги пусеты", BigDecimal.valueOf(219), 100, 0, "/images/3.png"),
                new StorageItemEntity("Инквизитор Рейвенор. Абнетт Дэн", BigDecimal.valueOf(1487), 25, 0, "/images/4.png"),
                new StorageItemEntity("Аквариум для рыбок круглый", BigDecimal.valueOf(571), 25, 0, "/images/5.png"),
                new StorageItemEntity("Кисточка для пупка", BigDecimal.valueOf(437), 100, 0, "/images/6.png")
        );
        storageItemRepository.saveAllAndFlush(testDataList);
    }

}
