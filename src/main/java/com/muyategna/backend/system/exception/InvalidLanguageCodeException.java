package com.muyategna.backend.system.exception;

public class InvalidLanguageCodeException extends RuntimeException{
    public InvalidLanguageCodeException() {
        super("Language code is invalid.");
    }

    public InvalidLanguageCodeException(String message) {
        super(message);
    }

    public InvalidLanguageCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLanguageCodeException(Throwable cause) {
        super(cause);
    }
}
