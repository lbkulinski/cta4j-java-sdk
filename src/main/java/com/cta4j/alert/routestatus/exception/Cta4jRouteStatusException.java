package com.cta4j.alert.routestatus.exception;

import com.cta4j.alert.common.exception.Cta4jAlertException;
import com.cta4j.alert.common.internal.util.AlertApiConstants;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A custom exception class for handling cta4j alert route status-specific errors.
 */
@NullMarked
public final class Cta4jRouteStatusException extends Cta4jAlertException {
    /**
     * The error code associated with this exception, if available.
     */
    @Nullable
    private final RouteStatusErrorCode errorCode;

    /**
     * Constructs a {@code Cta4jRouteStatusException}.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public Cta4jRouteStatusException(String message, Throwable cause) {
        super(message, AlertApiConstants.ROUTE_STATUS_ENDPOINT, cause);

        this.errorCode = null;
    }

    /**
     * Constructs a {@code Cta4jRouteStatusException}.
     *
     * @param message the detail message
     * @param rawErrorCode the raw error code associated with the exception
     */
    public Cta4jRouteStatusException(String message, int rawErrorCode) {
        super(message, AlertApiConstants.ROUTE_STATUS_ENDPOINT, rawErrorCode);

        this.errorCode = RouteStatusErrorCode.fromCode(rawErrorCode);
    }

    /**
     * Returns the error code associated with this exception, if available.
     *
     * @return the error code, or {@code null} if not available
     */
    public @Nullable RouteStatusErrorCode getErrorCode() {
        return this.errorCode;
    }
}
