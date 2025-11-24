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
        if (storageItemRepository.count() <= 0) {
            //инициализируем тестовыми данными
            List<StorageItemEntity> testDataList = List.of(
                    new StorageItemEntity("PlayStation 5", BigDecimal.valueOf(90000), 100, 0, "/images/cat.jpeg"),
                    new StorageItemEntity("SteamDeck", BigDecimal.valueOf(50000), 10, 0, "/images/cat.jpeg"),
                    new StorageItemEntity("X-BOX 360", BigDecimal.valueOf(100000), 20, 0, "/images/cat.jpeg")
            );
            storageItemRepository.saveAllAndFlush(testDataList);
        }
    }

}
