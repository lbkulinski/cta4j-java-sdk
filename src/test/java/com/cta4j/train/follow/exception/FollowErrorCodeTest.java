package com.cta4j.train.follow.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FollowErrorCodeTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(FollowErrorCode.fromCode(0)).isEqualTo(FollowErrorCode.OK);
        assertThat(FollowErrorCode.fromCode(101)).isEqualTo(FollowErrorCode.INVALID_API_KEY);
        assertThat(FollowErrorCode.fromCode(501)).isEqualTo(FollowErrorCode.RUN_NOT_FOUND);
        assertThat(FollowErrorCode.fromCode(503)).isEqualTo(FollowErrorCode.UNABLE_TO_FIND_PREDICTIONS);
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