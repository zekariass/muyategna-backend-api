package com.muyategna.backend.rabbitmq;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.rabbitmq.model.QueueSpec;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RabbitConfig {

    @Bean
    public List<QueueSpec> queueSpecs() {
        return List.of(
                QueueSpec.withBaseName(Constants.JOB_REQUEST_FILE_UPLOAD_QUEUE_BASE_NAME),
                QueueSpec.withBaseName(Constants.JOB_REQUEST_NOTIFICATION_QUEUE_BASE_NAME),
                QueueSpec.withBaseName(Constants.JOB_LEAD_GENERATION_QUEUE_BASE_NAME),
                QueueSpec.withBaseName(Constants.JOB_LEAD_REASSIGNMENT_QUEUE_BASE_NAME),
                QueueSpec.withBaseName(Constants.SP_RANKING_FOR_NEW_LEAD_ASSIGNMENT_QUEUE_BASE_NAME),
                QueueSpec.withBaseName(Constants.JOB_LEAD_REMINDER_QUEUE_BASE_NAME),
                QueueSpec.withBaseName(Constants.SP_RANKING_FOR_LEAD_REASSIGNMENT_QUEUE_BASE_NAME),
                QueueSpec.withBaseName(Constants.JOB_LEAD_ASSIGNMENT_QUEUE_BASE_NAME)

        );
    }


    @Bean
    public Declarables rabbitDeclarables(List<QueueSpec> queueSpecs) {
        List<Declarable> declarables = new ArrayList<>();

        for (QueueSpec queueSpec : queueSpecs) {
            // Exchanges
            DirectExchange mainExchange = new DirectExchange(queueSpec.exchange(), true, false);
            DirectExchange dlqExchange = new DirectExchange(queueSpec.dlqExchange(), true, false);

            // Queues
            Queue mainQueue = QueueBuilder
                    .durable(queueSpec.name())
                    .withArgument("x-dead-letter-exchange", queueSpec.dlqExchange())
                    .withArgument("x-dead-letter-routing-key", queueSpec.dlqRoutingKey())
                    .build();

            Queue dlq = QueueBuilder
                    .durable(queueSpec.dlqName())
                    .build();

            // Bindings
            Binding mainBinding = BindingBuilder
                    .bind(mainQueue)
                    .to(mainExchange)
                    .with(queueSpec.routingKey());

            Binding dlqBinding = BindingBuilder
                    .bind(dlq)
                    .to(dlqExchange)
                    .with(queueSpec.dlqRoutingKey());

            // Add to declarables
            declarables.add(mainExchange);
            declarables.add(dlqExchange);
            declarables.add(mainQueue);
            declarables.add(dlq);
            declarables.add(mainBinding);
            declarables.add(dlqBinding);
        }

        return new Declarables(declarables);
    }



    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
//            ConnectionFactory connectionFactory,
//            Jackson2JsonMessageConverter converter) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(converter);
//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//
//        // Concurrency settings
//        factory.setConcurrentConsumers(5);
//        factory.setMaxConcurrentConsumers(10);
//
//        // Retry configuration
//        RetryOperationsInterceptor retryInterceptor = RetryInterceptorBuilder.stateless()
//                .maxAttempts(5)
//                .backOffOptions(2000, 2.0, 10000)
//                .recoverer(new RejectAndDontRequeueRecoverer()) // to prevent infinite retries
//                .build();
//
//        factory.setAdviceChain(retryInterceptor);
//
//        return factory;
//    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        // Custom RetryTemplate with LoggingRetryListener
        RetryTemplate retryTemplate = new RetryTemplate();

        // Retry policy
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5);

        // Backoff policy
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(2000);  // 2 seconds
        backOffPolicy.setMultiplier(2.0);        // exponential
        backOffPolicy.setMaxInterval(10000);     // 10 seconds

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        // Register your custom retry listener
        retryTemplate.registerListener(new LoggingRetryListener());

        // Retry interceptor using the above template
        RetryOperationsInterceptor retryInterceptor = RetryInterceptorBuilder.stateless()
                .retryOperations(retryTemplate)
                .recoverer(new RejectAndDontRequeueRecoverer()) // fail fast after retries
                .build();

        // Configure factory
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(10);
        factory.setAdviceChain(retryInterceptor); // <--- Apply retry advice

        return factory;
    }

}
