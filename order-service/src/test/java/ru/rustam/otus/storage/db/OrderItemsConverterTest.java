package ru.rustam.otus.storage.db;

import org.junit.jupiter.api.Test;
import ru.rustam.otus.order.db.OrderItem;
import ru.rustam.otus.order.db.OrderItemsConverter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderItemsConverterTest {

    @Test
    void convertToDatabaseColumn() {
        OrderItemsConverter converter = new OrderItemsConverter();
        List<OrderItem> list = List.of(new OrderItem(1, 1), new OrderItem(2, 2));
        var str = converter.convertToDatabaseColumn(list);
        assertEquals("[{\"itemId\":1,\"count\":1},{\"itemId\":2,\"count\":2}]", str);
    }

    @Test
    void convertToEntityAttribute() {
        OrderItemsConverter converter = new OrderItemsConverter();
        var str = "[{\"itemId\":1,\"count\":1},{\"itemId\":2,\"count\":2}]";
        List<OrderItem> list = List.of(new OrderItem(1, 1), new OrderItem(2, 2));
        List<OrderItem> newList = converter.convertToEntityAttribute(str);
        assertThat(newList)
                .isNotNull()
                .hasSize(2)
                .isEqualTo(list);
    }
}