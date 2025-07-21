package com.muyategna.backend.rabbitmq.model;

public record QueueSpec(
        String name,
        String exchange,
        String routingKey,
        String dlqName,
        String dlqExchange,
        String dlqRoutingKey
        ) {

    public static QueueSpec withBaseName(String baseName) {
        return new QueueSpec(
                baseName + ".queue",
                baseName + ".exchange",
                baseName + ".routing.key",
                baseName + ".dlq",
                baseName + ".dlx",
                baseName + ".dlq.routing.key"
        );
    }
}
