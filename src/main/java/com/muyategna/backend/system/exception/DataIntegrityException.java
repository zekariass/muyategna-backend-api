package com.muyategna.backend.system.exception;

public class DataIntegrityException extends RuntimeException {
    public DataIntegrityException() {
        super("Data integrity exception.");
    }

    public DataIntegrityException(String message) {
        super(message);
    }

    public DataIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataIntegrityException(Throwable cause) {
        super(cause);
    }
}
