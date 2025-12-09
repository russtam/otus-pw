package ru.rustam.otus.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientMessage {
    private String userName;
    private String contactPhone;
    private String email;
    private String message;
}
