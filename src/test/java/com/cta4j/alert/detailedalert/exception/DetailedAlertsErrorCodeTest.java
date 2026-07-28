package com.cta4j.alert.detailedalert.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DetailedAlertsErrorCodeTest {
    @Test
    void fromCode_returnsCorrectValue_forEveryDefinedCode() {
        for (DetailedAlertsErrorCode code : DetailedAlertsErrorCode.values()) {
            assertThat(DetailedAlertsErrorCode.fromCode(code.getCode())).isEqualTo(code);
        }
    }

    @Test
    void fromCode_returnsUnknown_whenCodeIsUnrecognized() {
        assertThat(DetailedAlertsErrorCode.fromCode(12345)).isEqualTo(DetailedAlertsErrorCode.UNKNOWN);
    }

    @Test
    void getCode_returnsCode() {
        assertThat(DetailedAlertsErrorCode.NO_ACTIVE_ALERTS.getCode()).isEqualTo(25);
        assertThat(DetailedAlertsErrorCode.NO_ACTIVE_ALERTS_FOR_FILTER.getCode()).isEqualTo(50);
        assertThat(DetailedAlertsErrorCode.UNKNOWN.getCode()).isEqualTo(-1);
    }
}
