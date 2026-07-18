package com.cta4j.train.follow.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FollowErrorCodeTest {
    @Test
    void fromCode_returnsCorrectValue_forEveryDefinedCode() {
        for (FollowErrorCode code : FollowErrorCode.values()) {
            assertThat(FollowErrorCode.fromCode(code.getCode())).isEqualTo(code);
        }
    }

    @Test
    void fromCode_returnsUnknown_whenCodeIsUnrecognized() {
        assertThat(FollowErrorCode.fromCode(999)).isEqualTo(FollowErrorCode.UNKNOWN);
    }

    @Test
    void getCode_returnsCode() {
        assertThat(FollowErrorCode.RUN_NOT_FOUND.getCode()).isEqualTo(501);
        assertThat(FollowErrorCode.UNKNOWN.getCode()).isEqualTo(-1);
    }
}
