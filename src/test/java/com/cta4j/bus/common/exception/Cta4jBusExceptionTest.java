package com.cta4j.bus.common.exception;

import com.cta4j.bus.common.internal.wire.CtaError;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Cta4jBusExceptionTest {
    @Test
    void constructor_setsMessageAndEndpoint_whenErrorsHasOneElement() {
        CtaError error = () -> "No data found for route";

        Cta4jBusException exception = new Cta4jBusException(List.of(error), "/bustime/api/v3/getroutes");

        assertThat(exception.getMessage()).isEqualTo("No data found for route");
        assertThat(exception.getEndpoint()).isEqualTo("/bustime/api/v3/getroutes");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void constructor_joinsMultipleErrorMessages() {
        CtaError first = () -> "Invalid route";
        CtaError second = () -> "Unknown parameter";

        Cta4jBusException exception = new Cta4jBusException(List.of(first, second), "/bustime/api/v3/getroutes");

        assertThat(exception.getMessage()).isEqualTo("Invalid route; Unknown parameter");
    }

    @Test
    void constructor_returnsUnknownError_whenErrorsIsEmpty() {
        Cta4jBusException exception = new Cta4jBusException(List.of(), "/bustime/api/v3/getroutes");

        assertThat(exception.getMessage()).isEqualTo("Unknown error");
    }

    @Test
    void constructor_throwsNullPointerException_whenErrorsIsNull() {
        assertThatNullPointerException().isThrownBy(() ->
            new Cta4jBusException((List<? extends CtaError>) null, "/bustime/api/v3/getroutes"));
    }

    @Test
    void constructor_setsMessageEndpointAndCause() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jBusException exception = new Cta4jBusException("parse failed", "/bustime/api/v3/getroutes", cause);

        assertThat(exception.getMessage()).isEqualTo("parse failed");
        assertThat(exception.getEndpoint()).isEqualTo("/bustime/api/v3/getroutes");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void constructor_setsMessageAndEndpoint_withNoErrorsOrCause() {
        Cta4jBusException exception = new Cta4jBusException("Multiple stops found for ID: 123", "/bustime/api/v3/getstops");

        assertThat(exception.getMessage()).isEqualTo("Multiple stops found for ID: 123");
        assertThat(exception.getEndpoint()).isEqualTo("/bustime/api/v3/getstops");
        assertThat(exception.getCause()).isNull();
    }
}