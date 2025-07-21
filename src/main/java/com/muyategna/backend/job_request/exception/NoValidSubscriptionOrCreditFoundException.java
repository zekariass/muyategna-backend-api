package com.muyategna.backend.job_request.exception;

public class NoValidSubscriptionOrCreditFoundException extends RuntimeException {
    public NoValidSubscriptionOrCreditFoundException() {
        super("Valid subscription or enough credit not found for this service provider.");
    }

    public NoValidSubscriptionOrCreditFoundException(String message) {
        super(message);
    }

    public NoValidSubscriptionOrCreditFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoValidSubscriptionOrCreditFoundException(Throwable cause) {
        super(cause);
    }
}
