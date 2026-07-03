package com.cta4j.train.location.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class LocationsErrorCodeTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(LocationsErrorCode.fromCode(0)).isEqualTo(LocationsErrorCode.OK);
        assertThat(LocationsErrorCode.fromCode(101)).isEqualTo(LocationsErrorCode.INVALID_API_KEY);
        assertThat(LocationsErrorCode.fromCode(106)).isEqualTo(LocationsErrorCode.INVALID_ROUTE);
        assertThat(LocationsErrorCode.fromCode(107)).isEqualTo(LocationsErrorCode.TOO_MANY_ROUTES);
    }

    @Test
    void fromCode_returnsUnknown_whenCodeIsUnrecognized() {
        assertThat(LocationsErrorCode.fromCode(999)).isEqualTo(LocationsErrorCode.UNKNOWN);
    }

    @Test
    void getCode_returnsCode() {
        assertThat(LocationsErrorCode.INVALID_ROUTE.getCode()).isEqualTo(106);
        assertThat(LocationsErrorCode.UNKNOWN.getCode()).isEqualTo(-1);
    }
}