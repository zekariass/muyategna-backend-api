package com.muyategna.backend.location.exception;

/**
 * Exception thrown when a country is not found in the system.
 * This exception is used to indicate that a requested country does not exist.
 */
public class CountryNotFoundException extends RuntimeException{

        /**
         * Default constructor for UserCreationException.
         * Initializes a new instance of the exception without a message or cause.
         */
        public CountryNotFoundException() {
            super();
        }


        /**
         * Constructor for UserCreationException with a message.
         * Initializes a new instance of the exception with the specified message.
         *
         * @param message The message that describes the error.
         */
        public CountryNotFoundException(String message) {
            super(message);
        }


        /**
         * Constructor for UserCreationException with a message and cause.
         * Initializes a new instance of the exception with the specified message and cause.
         *
         * @param message The message that describes the error.
         * @param cause The cause of the error.
         */
        public CountryNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }


        /**
         * Constructor for UserCreationException with a cause.
         * Initializes a new instance of the exception with the specified cause.
         *
         * @param cause The cause of the error.
         */
        public CountryNotFoundException(Throwable cause) {
            super(cause);
        }
}
