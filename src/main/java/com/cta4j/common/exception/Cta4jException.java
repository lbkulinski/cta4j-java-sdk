package com.cta4j.common.exception;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A custom exception class for handling cta4j-specific errors.
 */
@NullMarked
public class Cta4jException extends RuntimeException {
    /**
     * The endpoint associated with this exception.
     */
    private final String endpoint;

    /**
     * Constructs a {@code Cta4jException}.
     *
     * @param message the detail message
     * @param endpoint the endpoint associated with the exception
     */
    public Cta4jException(String message, String endpoint) {
        super(message);

        this.endpoint = Objects.requireNonNull(endpoint);
    }

    /**
     * Constructs a {@code Cta4jException}.
     *
     * @param message the detail message
     * @param endpoint the endpoint associated with the exception
     * @param cause the cause of the exception
     */
    public Cta4jException(String message, String endpoint, Throwable cause) {
        super(message, cause);

        this.endpoint = Objects.requireNonNull(endpoint);
    }

    /**
     * Returns the endpoint associated with this exception.
     *
     * @return the endpoint
     */
    public String getEndpoint() {
        return this.endpoint;
    }
}
