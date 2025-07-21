package com.muyategna.backend.job_request.exception;

public class ServiceNotSubscribedException extends RuntimeException {
    public ServiceNotSubscribedException() {
        super("Service Provider does not have this service subscribed.");
    }

    public ServiceNotSubscribedException(String message) {
        super(message);
    }

    public ServiceNotSubscribedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotSubscribedException(Throwable cause) {
        super(cause);
    }
}
