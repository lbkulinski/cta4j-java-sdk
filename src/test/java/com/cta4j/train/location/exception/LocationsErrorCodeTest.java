package com.cta4j.train.location.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class LocationsErrorCodeTest {
    @Test
    void fromCode_returnsCorrectValue_forEveryDefinedCode() {
        for (LocationsErrorCode code : LocationsErrorCode.values()) {
            assertThat(LocationsErrorCode.fromCode(code.getCode())).isEqualTo(code);
        }
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
