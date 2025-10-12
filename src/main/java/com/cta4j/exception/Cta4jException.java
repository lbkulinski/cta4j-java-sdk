package com.cta4j.exception;

/**
 * A custom exception class for handling cta4j specific errors.
 */
public class Cta4jException extends RuntimeException {
    /**
     * Constructs a new Cta4jException with the specified detail message.
     *
     * @param message the detail message
     */
    public Cta4jException(String message) {
        super(message);
    }

    /**
     * Constructs a new Cta4jException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public Cta4jException(String message, Throwable cause) {
        super(message, cause);
    }
}
