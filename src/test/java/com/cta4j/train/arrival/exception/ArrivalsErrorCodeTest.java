package com.cta4j.train.arrival.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArrivalsErrorCodeTest {
    @Test
    void fromCode_returnsCorrectValue_forEveryDefinedCode() {
        for (ArrivalsErrorCode code : ArrivalsErrorCode.values()) {
            assertThat(ArrivalsErrorCode.fromCode(code.getCode())).isEqualTo(code);
        }
    }

    @Test
    void fromCode_returnsNull_whenCodeIsUnrecognized() {
        assertThat(ArrivalsErrorCode.fromCode(999)).isNull();
    }

    @Test
    void getCode_returnsCode() {
        assertThat(ArrivalsErrorCode.INVALID_API_KEY.getCode()).isEqualTo(101);
    }
}
