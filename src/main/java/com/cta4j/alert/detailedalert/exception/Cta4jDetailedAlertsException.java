package com.cta4j.alert.detailedalert.exception;

import com.cta4j.alert.common.exception.Cta4jAlertException;
import com.cta4j.alert.common.internal.util.AlertApiConstants;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// A custom exception class for handling cta4j detailed alerts-specific errors.
@NullMarked
public final class Cta4jDetailedAlertsException extends Cta4jAlertException {
    @Nullable
    private final DetailedAlertsErrorCode errorCode;

    /// Constructs a `Cta4jDetailedAlertsException`.
    ///
    /// @param message the detail message
    /// @param cause the cause of the exception
    public Cta4jDetailedAlertsException(String message, Throwable cause) {
        super(message, AlertApiConstants.DETAILED_ALERTS_ENDPOINT, cause);

        this.errorCode = null;
    }

    /// Constructs a `Cta4jDetailedAlertsException`.
    ///
    /// @param message the detail message
    /// @param rawErrorCode the raw error code associated with the exception
    public Cta4jDetailedAlertsException(String message, int rawErrorCode) {
        super(message, AlertApiConstants.DETAILED_ALERTS_ENDPOINT, rawErrorCode);

        this.errorCode = DetailedAlertsErrorCode.fromCode(rawErrorCode);
    }

    /// Returns the error code associated with this exception, if available.
    ///
    /// @return the error code, or `null` if not available
    public @Nullable DetailedAlertsErrorCode getErrorCode() {
        return this.errorCode;
    }
}
