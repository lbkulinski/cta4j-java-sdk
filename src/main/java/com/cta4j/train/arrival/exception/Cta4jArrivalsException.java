package com.cta4j.train.arrival.exception;

import com.cta4j.train.common.exception.Cta4jTrainException;
import com.cta4j.train.common.internal.util.TrainApiConstants;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// A custom exception class for handling cta4j train arrival-specific errors.
@NullMarked
public final class Cta4jArrivalsException extends Cta4jTrainException {
    @Nullable
    private final ArrivalsErrorCode errorCode;

    /// Constructs a `Cta4jArrivalsException`.
    ///
    /// @param message the detail message
    /// @param cause the cause of the exception
    public Cta4jArrivalsException(String message, Throwable cause) {
        super(message, TrainApiConstants.ARRIVALS_ENDPOINT, cause);

        this.errorCode = null;
    }

    /// Constructs a `Cta4jArrivalsException`.
    ///
    /// @param message the detail message
    /// @param rawErrorCode the raw error code associated with the exception
    public Cta4jArrivalsException(String message, int rawErrorCode) {
        super(message, TrainApiConstants.ARRIVALS_ENDPOINT, rawErrorCode);

        this.errorCode = ArrivalsErrorCode.fromCode(rawErrorCode);
    }

    /// Returns the error code associated with this exception, if available.
    ///
    /// @return the error code, if available
    public @Nullable ArrivalsErrorCode getErrorCode() {
        return this.errorCode;
    }
}
