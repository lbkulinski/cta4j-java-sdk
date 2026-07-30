package com.cta4j.common.exception;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/// A custom exception type for handling SDK-specific errors.
@NullMarked
public class Cta4jException extends RuntimeException {
    private final String endpoint;

    /// Constructs a `Cta4jException`.
    ///
    /// @param message the detail message
    /// @param endpoint the URL of the API endpoint associated with the exception
    /// @throws NullPointerException if `endpoint` is `null`
    public Cta4jException(String message, String endpoint) {
        super(message);

        this.endpoint = Objects.requireNonNull(endpoint);
    }

    /// Constructs a `Cta4jException`.
    ///
    /// @param message the detail message
    /// @param endpoint the URL of the API endpoint associated with the exception
    /// @param cause the cause of the exception
    /// @throws NullPointerException if `endpoint` is `null`
    public Cta4jException(String message, String endpoint, Throwable cause) {
        super(message, cause);

        this.endpoint = Objects.requireNonNull(endpoint);
    }

    /// Returns the URL of the API endpoint associated with this exception.
    ///
    /// @return the endpoint URL
    public String getEndpoint() {
        return this.endpoint;
    }
}
