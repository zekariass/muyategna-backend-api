package com.muyategna.backend.storage.exception;

public class UnsupportedMediaTypeException extends RuntimeException {
    public UnsupportedMediaTypeException() {
        super("Unsupported media type.");
    }

    public UnsupportedMediaTypeException(String message) {
        super(message);
    }

    public UnsupportedMediaTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedMediaTypeException(Throwable cause) {
        super(cause);
    }
}
