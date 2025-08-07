package com.muyategna.backend.job_request.exception;

public class JobLeadClaimStateInvalidException extends RuntimeException {
    public JobLeadClaimStateInvalidException() {
        super("Job lead claim state is invalid.");
    }

    public JobLeadClaimStateInvalidException(String message) {
        super(message);
    }

    public JobLeadClaimStateInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobLeadClaimStateInvalidException(Throwable cause) {
        super(cause);
    }
}
