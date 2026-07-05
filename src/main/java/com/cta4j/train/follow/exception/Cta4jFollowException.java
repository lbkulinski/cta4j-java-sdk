package com.cta4j.train.follow.exception;

import com.cta4j.train.common.exception.Cta4jTrainException;
import com.cta4j.train.common.internal.util.TrainApiConstants;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A custom exception class for handling cta4j train follow-specific errors.
 */
@NullMarked
public final class Cta4jFollowException extends Cta4jTrainException {
    /**
     * The error code associated with this exception, if available.
     */
    @Nullable
    private final FollowErrorCode errorCode;

    /**
     * Constructs a {@code Cta4jFollowException}.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public Cta4jFollowException(String message, Throwable cause) {
        super(message, TrainApiConstants.FOLLOW_ENDPOINT, cause);

        this.errorCode = null;
    }

    /**
     * Constructs a {@code Cta4jFollowException}.
     *
     * @param message the detail message
     * @param rawErrorCode the raw error code associated with the exception
     */
    public Cta4jFollowException(String message, int rawErrorCode) {
        super(message, TrainApiConstants.FOLLOW_ENDPOINT, rawErrorCode);

        this.errorCode = FollowErrorCode.fromCode(rawErrorCode);
    }

    /**
     * Returns the error code associated with this exception, if available.
     *
     * @return the error code, or {@code null} if not available
     */
    public @Nullable FollowErrorCode getErrorCode() {
        return this.errorCode;
    }
}
