package ru.rustam.otus.storage.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageItemRepository extends JpaRepository<StorageItemEntity, Long> {
}
