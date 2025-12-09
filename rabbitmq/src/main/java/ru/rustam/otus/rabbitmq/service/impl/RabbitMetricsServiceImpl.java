package ru.rustam.otus.rabbitmq.service.impl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import ru.rustam.otus.rabbitmq.service.RabbitMetricsService;

@Service
public class RabbitMetricsServiceImpl implements RabbitMetricsService {

    private final Counter customMetricCounter;

    public RabbitMetricsServiceImpl(MeterRegistry meterRegistry) {
        this.customMetricCounter = Counter.builder("rabbit_messages_sent")
                .description("Count of messages sent to RabbitMQ")
                .register(meterRegistry);
    }

    @Override
    public void increaseMessageSent() {
        customMetricCounter.increment();
    }

}
