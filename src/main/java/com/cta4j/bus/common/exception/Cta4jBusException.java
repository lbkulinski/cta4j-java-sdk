package com.cta4j.bus.common.exception;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.cta4j.common.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

/// A custom exception class for handling cta4j bus-specific errors.
@NullMarked
public final class Cta4jBusException extends Cta4jException {
    /// Constructs a `Cta4jBusException`.
    ///
    /// @param message the detail message
    /// @param endpoint the endpoint associated with the exception
    /// @throws NullPointerException if `endpoint` is `null`
    public Cta4jBusException(String message, String endpoint) {
        super(message, endpoint);
    }

    /// Constructs a `Cta4jBusException`.
    ///
    /// @param message the detail message
    /// @param endpoint the endpoint associated with the exception
    /// @param cause the cause of the exception
    /// @throws NullPointerException if `endpoint` is `null`
    public Cta4jBusException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);
    }

    /// Constructs a `Cta4jBusException`.
    ///
    /// @param errors the list of [CtaError] objects
    /// @param endpoint the endpoint associated with the exception
    /// @throws NullPointerException if `errors` or `endpoint` is `null`, or if any element of `errors` is `null`
    public Cta4jBusException(List<? extends CtaError> errors, String endpoint) {
        super(joinMessages(errors), endpoint);
    }

    private static String joinMessages(List<? extends CtaError> errors) {
        Objects.requireNonNull(errors);

        return List.copyOf(errors)
                   .stream()
                   .map(CtaError::msg)
                   .reduce("%s; %s"::formatted)
                   .orElse("Unknown error");
    }
}
