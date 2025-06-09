package com.muyategna.backend.system.exception;

public class UserDoesNotHaveDefaultLanguageException extends RuntimeException{
    public UserDoesNotHaveDefaultLanguageException() {
        super("User does not have a default language set.");
    }

    public UserDoesNotHaveDefaultLanguageException(String message) {
        super(message);
    }

    public UserDoesNotHaveDefaultLanguageException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesNotHaveDefaultLanguageException(Throwable cause) {
        super(cause);
    }
}
