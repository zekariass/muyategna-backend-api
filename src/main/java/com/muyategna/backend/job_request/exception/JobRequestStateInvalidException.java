package com.muyategna.backend.job_request.exception;

public class JobRequestStateInvalidException extends RuntimeException {
    public JobRequestStateInvalidException() {
        super("Job request state is invalid.");
    }

    public JobRequestStateInvalidException(String message) {
        super(message);
    }

    public JobRequestStateInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobRequestStateInvalidException(Throwable cause) {
        super(cause);
    }
}
