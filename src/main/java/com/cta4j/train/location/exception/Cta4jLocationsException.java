package com.cta4j.train.location.exception;

import com.cta4j.train.common.TrainApiConstants;
import com.cta4j.train.common.exception.Cta4jTrainException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A custom exception class for handling cta4j train location-specific errors.
 */
@NullMarked
public final class Cta4jLocationsException extends Cta4jTrainException {
    /**
     * The error code associated with this exception, if available.
     */
    @Nullable
    private final LocationsErrorCode errorCode;

    /**
     * Constructs a {@code Cta4jLocationsException}.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public Cta4jLocationsException(String message, Throwable cause) {
        super(message, TrainApiConstants.POSITIONS_ENDPOINT, cause);

        this.errorCode = null;
    }

    /**
     * Constructs a {@code Cta4jLocationsException}.
     *
     * @param message the detail message
     * @param rawErrorCode the raw error code associated with the exception
     */
    public Cta4jLocationsException(String message, int rawErrorCode) {
        super(message, TrainApiConstants.POSITIONS_ENDPOINT, rawErrorCode);

        this.errorCode = LocationsErrorCode.fromCode(rawErrorCode);
    }

    /**
     * Returns the error code associated with this exception, if available.
     *
     * @return the error code, or {@code null} if not available
     */
    public @Nullable LocationsErrorCode getErrorCode() {
        return this.errorCode;
    }
}
