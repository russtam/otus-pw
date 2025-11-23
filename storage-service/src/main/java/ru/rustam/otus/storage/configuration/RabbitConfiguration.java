package ru.rustam.otus.storage.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.FAIL_FANOUT_EXCHANGE;

@Configuration
public class RabbitConfiguration {

    public static final String FAIL_QUEUE = "FailQueue-StorageService";

    @Autowired
    private AmqpAdmin amqpAdmin;

    @PostConstruct
    public void postConstruct() {
        var queue = new Queue(FAIL_QUEUE, false);
        amqpAdmin.declareQueue(queue);
        var exchange = new FanoutExchange(FAIL_FANOUT_EXCHANGE);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange));
    }

}
