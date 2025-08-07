package com.muyategna.backend.job_request.exception;

public class JobQuoteStateInvalidException extends RuntimeException {
    public JobQuoteStateInvalidException() {
        super("Job quote state is invalid.");
    }

    public JobQuoteStateInvalidException(String message) {
        super(message);
    }

    public JobQuoteStateInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobQuoteStateInvalidException(Throwable cause) {
        super(cause);
    }
}
