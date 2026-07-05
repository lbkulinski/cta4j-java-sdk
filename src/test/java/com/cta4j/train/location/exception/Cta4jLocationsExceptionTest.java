package com.cta4j.train.location.exception;

import com.cta4j.train.common.internal.util.TrainApiConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jLocationsExceptionTest {
    @Test
    void constructor_setsMessageEndpointAndCause_andLeavesErrorCodeNull() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jLocationsException exception = new Cta4jLocationsException("Failed to parse response", cause);

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.getEndpoint()).isEqualTo(TrainApiConstants.POSITIONS_ENDPOINT);
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getRawErrorCode()).isNull();
        assertThat(exception.getErrorCode()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndErrorCode() {
        Cta4jLocationsException exception = new Cta4jLocationsException("Invalid API key", 101);

        assertThat(exception.getMessage()).isEqualTo("Invalid API key");
        assertThat(exception.getEndpoint()).isEqualTo(TrainApiConstants.POSITIONS_ENDPOINT);
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getRawErrorCode()).isEqualTo(101);
        assertThat(exception.getErrorCode()).isEqualTo(LocationsErrorCode.INVALID_API_KEY);
    }

    @Test
    void constructor_setsUnknownErrorCode_whenRawErrorCodeIsUnrecognized() {
        Cta4jLocationsException exception = new Cta4jLocationsException("Something odd happened", 999);

        assertThat(exception.getRawErrorCode()).isEqualTo(999);
        assertThat(exception.getErrorCode()).isEqualTo(LocationsErrorCode.UNKNOWN);
    }
}