package com.muyategna.backend.rabbitmq;

import com.muyategna.backend.job_request.exception.NoValidSubscriptionOrCreditFoundException;
import com.muyategna.backend.job_request.exception.ServiceNotSubscribedException;
import com.muyategna.backend.system.exception.InvalidLanguageCodeException;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.exception.UserDoesNotHaveDefaultLanguageException;
import com.muyategna.backend.user.exception.DuplicateUserException;
import com.muyategna.backend.user.exception.UserCreationException;
import jakarta.ws.rs.BadRequestException;
import org.hibernate.LazyInitializationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import software.amazon.awssdk.services.ses.model.MessageRejectedException;

import java.util.Set;

public class RabbitUtils {
    private static final Set<Class<?>> UN_RETRYABLE_EXCEPTIONS = Set.of(
            MessageRejectedException.class,
            ResourceNotFoundException.class,
            DataIntegrityViolationException.class,
            IllegalArgumentException.class,
            NullPointerException.class,
            NoValidSubscriptionOrCreditFoundException.class,
            ServiceNotSubscribedException.class,
            InvalidLanguageCodeException.class,
            UserDoesNotHaveDefaultLanguageException.class,
            UserCreationException.class,
            BadRequestException.class,
            DuplicateUserException.class,
            MethodArgumentNotValidException.class,
            LazyInitializationException.class
    );

    // Check if the exception is retryable
    public static boolean isRetryable(Throwable throwable) {
        Throwable cause = getRootCause(throwable);
        return UN_RETRYABLE_EXCEPTIONS.stream().noneMatch(ex -> ex.isAssignableFrom(cause.getClass()));
    }

    // Get the root cause
    private static Throwable getRootCause(Throwable throwable) {
        Throwable result = throwable;
        while (result.getCause() != null && result.getCause() != result) {
            result = result.getCause();
        }
        return result;
    }

}
