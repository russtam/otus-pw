package ru.rustam.otus.storage.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.rustam.otus.storage.db.StorageItemEntity;
import ru.rustam.otus.storage.db.StorageItemRepository;

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
                    new StorageItemEntity("PlayStation 5", 100, 0),
                    new StorageItemEntity("SteamDeck", 10, 0),
                    new StorageItemEntity("X-BOX 360", 20, 0)
            );
            storageItemRepository.saveAllAndFlush(testDataList);
        }
    }

}
