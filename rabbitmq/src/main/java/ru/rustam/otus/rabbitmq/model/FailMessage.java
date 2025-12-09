package ru.rustam.otus.rabbitmq.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FailMessage {
    private String orderId;
    private String error;
    private String source;
}
