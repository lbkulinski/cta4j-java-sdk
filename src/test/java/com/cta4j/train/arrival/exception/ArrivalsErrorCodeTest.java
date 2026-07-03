package com.cta4j.train.arrival.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArrivalsErrorCodeTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(ArrivalsErrorCode.fromCode(0)).isEqualTo(ArrivalsErrorCode.OK);
        assertThat(ArrivalsErrorCode.fromCode(101)).isEqualTo(ArrivalsErrorCode.INVALID_API_KEY);
        assertThat(ArrivalsErrorCode.fromCode(103)).isEqualTo(ArrivalsErrorCode.INVALID_MAPID);
        assertThat(ArrivalsErrorCode.fromCode(108)).isEqualTo(ArrivalsErrorCode.INVALID_STPID);
        assertThat(ArrivalsErrorCode.fromCode(900)).isEqualTo(ArrivalsErrorCode.SERVER_ERROR);
    }

    @Test
    void fromCode_returnsUnknown_whenCodeIsUnrecognized() {
        assertThat(ArrivalsErrorCode.fromCode(999)).isEqualTo(ArrivalsErrorCode.UNKNOWN);
    }

    @Test
    void getCode_returnsCode() {
        assertThat(ArrivalsErrorCode.INVALID_API_KEY.getCode()).isEqualTo(101);
        assertThat(ArrivalsErrorCode.UNKNOWN.getCode()).isEqualTo(-1);
    }
}