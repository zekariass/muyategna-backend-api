package com.muyategna.backend.user.exception;

public class UserProfileDoesNotExistException extends RuntimeException{
    public UserProfileDoesNotExistException() {
        super("User profile does not exist.");
    }

    public UserProfileDoesNotExistException(String message) {
        super(message);
    }

    public UserProfileDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserProfileDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
