package com.muyategna.backend.rabbitmq;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingRetryListener implements RetryListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingRetryListener.class);

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {}

    @Override
    public <T, E extends Throwable> void onError(
            RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> RabbitMQ retry attempt {} failed for message. Reason: {}",
                context.getRetryCount(), throwable.getMessage());
    }
}
