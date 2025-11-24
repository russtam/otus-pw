package ru.rustam.otus.storage;

import lombok.experimental.UtilityClass;
import ru.rustam.otus.storage.db.StorageItemEntity;
import ru.rustam.otus.storage.model.StorageItemDto;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class ConvertUtil {

    public static StorageItemDto convertStorageItem(StorageItemEntity src) {
        StorageItemDto dst = new StorageItemDto();
        dst.setItemId(src.getItemId());
        dst.setAvailable(src.getAvailable());
        dst.setReserved(src.getReserved());
        dst.setName(src.getName());
        dst.setPrice(src.getPrice());
        dst.setImageUrl(src.getImageUrl());
        return dst;
    }

    public static List<StorageItemDto> convertStorageList(List<StorageItemEntity> src) {
        if (src == null) {
            return null;
        }
        if (src.isEmpty()) {
            return Collections.emptyList();
        }
        return src.stream().map(ConvertUtil::convertStorageItem).toList();
    }

}
