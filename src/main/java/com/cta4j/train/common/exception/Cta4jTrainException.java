package com.cta4j.train.common.exception;

import com.cta4j.common.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// A custom exception class for handling cta4j train-specific errors.
@NullMarked
public class Cta4jTrainException extends Cta4jException {
    @Nullable
    private final Integer rawErrorCode;

    /// Constructs a `Cta4jTrainException`.
    ///
    /// @param message the detail message
    /// @param endpoint the endpoint associated with the exception
    /// @throws NullPointerException if `endpoint` is `null`
    public Cta4jTrainException(String message, String endpoint) {
        super(message, endpoint);

        this.rawErrorCode = null;
    }

    /// Constructs a `Cta4jTrainException`.
    ///
    /// @param message the detail message
    /// @param endpoint the endpoint associated with the exception
    /// @param cause the cause of the exception
    /// @throws NullPointerException if `endpoint` is `null`
    public Cta4jTrainException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);

        this.rawErrorCode = null;
    }

    /// Constructs a `Cta4jTrainException` with a raw error code.
    ///
    /// @param message the detail message
    /// @param endpoint the endpoint associated with the exception
    /// @param rawErrorCode the raw error code associated with the exception
    /// @throws NullPointerException if `endpoint` is `null`
    public Cta4jTrainException(String message, String endpoint, int rawErrorCode) {
        super(message, endpoint);

        this.rawErrorCode = rawErrorCode;
    }

    /// Returns the raw error code associated with this exception, if available.
    ///
    /// @return the raw error code, or `null` if not available
    public @Nullable Integer getRawErrorCode() {
        return this.rawErrorCode;
    }
}
