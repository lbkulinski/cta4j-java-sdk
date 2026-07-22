package com.cta4j.alert.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jAlertExceptionTest {
    @Test
    void constructor_setsMessageAndEndpoint() {
        Cta4jAlertException exception = new Cta4jAlertException("Failed to parse response", "/routes.aspx");

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.getEndpoint()).isEqualTo("/routes.aspx");
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getRawErrorCode()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndCause() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jAlertException exception = new Cta4jAlertException("Failed to parse response", "/routes.aspx", cause);

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.getEndpoint()).isEqualTo("/routes.aspx");
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getRawErrorCode()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndRawErrorCode() {
        Cta4jAlertException exception = new Cta4jAlertException("Invalid parameter", "/routes.aspx", 500);

        assertThat(exception.getMessage()).isEqualTo("Invalid parameter");
        assertThat(exception.getEndpoint()).isEqualTo("/routes.aspx");
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getRawErrorCode()).isEqualTo(500);
    }
}
