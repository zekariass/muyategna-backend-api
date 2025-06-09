package com.muyategna.backend.common.exception;

public class TranslationNotFoundException extends RuntimeException {
    public TranslationNotFoundException() {
        super("Translation not found.");
    }

    public TranslationNotFoundException(String message) {
        super(message);
    }

    public TranslationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TranslationNotFoundException(Throwable cause) {
        super(cause);
    }
}
