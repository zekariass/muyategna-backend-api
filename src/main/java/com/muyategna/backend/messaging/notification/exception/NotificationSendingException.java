package com.muyategna.backend.messaging.notification.exception;

public class NotificationSendingException extends RuntimeException {
    public NotificationSendingException() {
        super("Failed to send email.");
    }

    public NotificationSendingException(String message) {
        super(message);
    }

    public NotificationSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationSendingException(Throwable cause) {
        super(cause);
    }
}
