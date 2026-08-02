package com.cta4j.train.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jTrainExceptionTest {
    @Test
    void constructor_setsMessageAndEndpoint() {
        Cta4jTrainException exception = new Cta4jTrainException("Failed to parse response", "/stations");

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.getEndpoint()).isEqualTo("/stations");
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getRawErrorCode()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndCause() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jTrainException exception = new Cta4jTrainException("Failed to parse response", "/stations", cause);

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.getEndpoint()).isEqualTo("/stations");
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getRawErrorCode()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndRawErrorCode() {
        Cta4jTrainException exception = new Cta4jTrainException("Invalid API key", "/stations", 101);

        assertThat(exception.getMessage()).isEqualTo("Invalid API key");
        assertThat(exception.getEndpoint()).isEqualTo("/stations");
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getRawErrorCode()).isEqualTo(101);
    }
}
