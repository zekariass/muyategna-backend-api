package com.muyategna.backend.user.exception;

/**
 * Exception thrown when a specified role is not found in the system.
 * This exception is typically used to indicate that an operation
 * requiring a specific role has failed because the role does not exist.
 */
public class RoleDoesNotFoundException extends RuntimeException{

    /**
     * Default constructor for RoleDoesNotFoundException.
     * Initializes a new instance of the exception without a message or cause.
     */
    public RoleDoesNotFoundException() {
        super();
    }

    /**
     * Constructor for RoleDoesNotFoundException with a message.
     * Initializes a new instance of the exception with the specified message.
     *
     * @param message The message that describes the error.
     */
    public RoleDoesNotFoundException(String message) {
        super(message);
    }


    /**
     * Constructor for RoleDoesNotFoundException with a message and cause.
     * Initializes a new instance of the exception with the specified message and cause.
     *
     * @param message The message that describes the error.
     * @param cause The cause of the error.
     */
    public RoleDoesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Constructor for RoleDoesNotFoundException with a cause.
     * Initializes a new instance of the exception with the specified cause.
     *
     * @param cause The cause of the error.
     */
    public RoleDoesNotFoundException(Throwable cause) {
        super(cause);
    }
}
