package ru.rustam.otus.common.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;

class OrderDtoTest {

    private static final String JSON = """
            {
              "amount": 1280.90,
              "items": [ {"itemId": 1,"count": 1}, {"itemId": 2, "count": 1} ],
              "deliveryAddress": "Санкт-Петербург, Камышовая ул., д.4, кв.5",
              "contactPhone": "79008007060"
            }
            """;

    @Test
    void deserializeTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        var order = objectMapper.readValue(JSON, OrderDto.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(new BigDecimal("1280.90"));
        assertThat(order.getDeliveryAddress()).isEqualTo("Санкт-Петербург, Камышовая ул., д.4, кв.5");
        assertThat(order.getContactPhone()).isEqualTo("79008007060");
        assertThat(order.getItems())
                .isNotNull()
                .hasSize(2);
    }

}