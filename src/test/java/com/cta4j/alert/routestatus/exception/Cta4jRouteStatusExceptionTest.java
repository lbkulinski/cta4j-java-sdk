package com.cta4j.alert.routestatus.exception;

import com.cta4j.alert.common.internal.util.AlertApiConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jRouteStatusExceptionTest {
    @Test
    void constructor_setsMessageEndpointAndCause_andLeavesErrorCodeNull() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jRouteStatusException exception = new Cta4jRouteStatusException("Failed to parse response", cause);

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.getEndpoint()).isEqualTo(AlertApiConstants.ROUTE_STATUS_ENDPOINT);
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getRawErrorCode()).isNull();
        assertThat(exception.getErrorCode()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndErrorCode() {
        Cta4jRouteStatusException exception =
            new Cta4jRouteStatusException("Invalid option for parameter 'type'", 101);

        assertThat(exception.getMessage()).isEqualTo("Invalid option for parameter 'type'");
        assertThat(exception.getEndpoint()).isEqualTo(AlertApiConstants.ROUTE_STATUS_ENDPOINT);
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getRawErrorCode()).isEqualTo(101);
        assertThat(exception.getErrorCode()).isEqualTo(RouteStatusErrorCode.INVALID_TYPE);
    }

    @Test
    void constructor_setsUnknownErrorCode_whenRawErrorCodeIsUnrecognized() {
        Cta4jRouteStatusException exception = new Cta4jRouteStatusException("Something odd happened", 999);

        assertThat(exception.getRawErrorCode()).isEqualTo(999);
        assertThat(exception.getErrorCode()).isEqualTo(RouteStatusErrorCode.UNKNOWN);
    }
}
