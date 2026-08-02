package com.cta4j.alert.detailedalert.exception;

import com.cta4j.alert.common.internal.util.AlertApiConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jDetailedAlertsExceptionTest {
    @Test
    void constructor_setsMessageEndpointAndCause_andLeavesErrorCodeNull() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jDetailedAlertsException exception = new Cta4jDetailedAlertsException("Failed to parse response", cause);

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.getEndpoint()).isEqualTo(AlertApiConstants.DETAILED_ALERTS_ENDPOINT);
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getRawErrorCode()).isNull();
        assertThat(exception.getErrorCode()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndErrorCode() {
        Cta4jDetailedAlertsException exception = new Cta4jDetailedAlertsException(
            "Invalid option for parameter 'activeonly': Valid options are 'true', 'false'", 100
        );

        assertThat(exception.getMessage())
            .isEqualTo("Invalid option for parameter 'activeonly': Valid options are 'true', 'false'");
        assertThat(exception.getEndpoint()).isEqualTo(AlertApiConstants.DETAILED_ALERTS_ENDPOINT);
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getRawErrorCode()).isEqualTo(100);
        assertThat(exception.getErrorCode()).isEqualTo(DetailedAlertsErrorCode.INVALID_ACTIVEONLY);
    }

    @Test
    void constructor_setsNullErrorCode_whenRawErrorCodeIsUnrecognized() {
        Cta4jDetailedAlertsException exception = new Cta4jDetailedAlertsException("Something odd happened", 999);

        assertThat(exception.getRawErrorCode()).isEqualTo(999);
        assertThat(exception.getErrorCode()).isNull();
    }
}
