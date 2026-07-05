package com.cta4j.bus.common.exception;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.cta4j.common.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

/**
 * A custom exception class for handling cta4j bus-specific errors.
 */
@NullMarked
public final class Cta4jBusException extends Cta4jException {
    /**
     * Constructs a {@code Cta4jBusException}.
     *
     * @param message the detail message
     * @param endpoint the endpoint associated with the exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jBusException(String message, String endpoint) {
        super(message, endpoint);
    }

    /**
     * Constructs a {@code Cta4jBusException}.
     *
     * @param message the detail message
     * @param endpoint the endpoint associated with the exception
     * @param cause the cause of the exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jBusException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);
    }

    /**
     * Constructs a {@code Cta4jBusException}.
     *
     * @param errors the list of {@link CtaError} objects
     * @param endpoint the endpoint associated with the exception
     * @throws NullPointerException if {@code errors} or {@code endpoint} is {@code null}, or if {@code errors}
     * contains {@code null} elements
     */
    public Cta4jBusException(List<? extends CtaError> errors, String endpoint) {
        super(joinMessages(errors), endpoint);
    }

    /**
     * Joins the messages from a list of {@link CtaError} objects into a single string.
     *
     * @param errors the list of {@link CtaError} objects
     * @return a single string containing all error messages, separated by "; "
     */
    private static String joinMessages(List<? extends CtaError> errors) {
        Objects.requireNonNull(errors);

        return List.copyOf(errors)
                   .stream()
                   .map(CtaError::msg)
                   .reduce("%s; %s"::formatted)
                   .orElse("Unknown error");
    }
}
