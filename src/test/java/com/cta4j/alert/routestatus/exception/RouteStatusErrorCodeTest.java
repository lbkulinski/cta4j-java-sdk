package com.cta4j.alert.routestatus.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RouteStatusErrorCodeTest {
    @Test
    void fromCode_returnsCorrectValue_forEveryDefinedCode() {
        for (RouteStatusErrorCode code : RouteStatusErrorCode.values()) {
            assertThat(RouteStatusErrorCode.fromCode(code.getCode())).isEqualTo(code);
        }
    }

    @Test
    void fromCode_returnsUnknown_whenCodeIsUnrecognized() {
        assertThat(RouteStatusErrorCode.fromCode(12345)).isEqualTo(RouteStatusErrorCode.UNKNOWN);
    }

    @Test
    void getCode_returnsCode() {
        assertThat(RouteStatusErrorCode.NO_RESULTS.getCode()).isEqualTo(50);
        assertThat(RouteStatusErrorCode.UNKNOWN.getCode()).isEqualTo(-1);
    }
}
