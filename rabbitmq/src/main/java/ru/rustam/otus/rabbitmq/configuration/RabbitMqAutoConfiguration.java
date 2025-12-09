package ru.rustam.otus.rabbitmq.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static ru.rustam.otus.rabbitmq.configuration.QueueConst.CLIENT_MESSAGE_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.DELIVERY_STARTED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.FANOUT_EXCHANGE_POSTFIX;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_CREATED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.ORDER_RESERVED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_COMPLETED_QUEUE;
import static ru.rustam.otus.rabbitmq.configuration.QueueConst.PAYMENT_RESULT_QUEUE;

@Configuration
@ComponentScan("ru.rustam.otus.rabbitmq")
public class RabbitMqAutoConfiguration {

    private static final List<String> QUEUES =
            List.of(ORDER_CREATED_QUEUE, ORDER_RESERVED_QUEUE, PAYMENT_COMPLETED_QUEUE,
                    CLIENT_MESSAGE_QUEUE, PAYMENT_RESULT_QUEUE, DELIVERY_STARTED_QUEUE);

    @Autowired
    private AmqpAdmin amqpAdmin;

    @PostConstruct
    public void postConstruct() {
        for (String queueName : QUEUES) {
            var queue = new Queue(queueName, false);
            amqpAdmin.declareQueue(queue);
            var exchange = new FanoutExchange(queueName + FANOUT_EXCHANGE_POSTFIX);
            amqpAdmin.declareExchange(exchange);
            amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange));
        }
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
