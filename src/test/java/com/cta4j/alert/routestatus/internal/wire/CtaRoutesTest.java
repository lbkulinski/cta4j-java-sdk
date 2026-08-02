package com.cta4j.alert.routestatus.internal.wire;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CtaRoutesTest {
    @Test
    void constructor_copiesErrorCode_whenNonNull() {
        List<String> errorCode = new ArrayList<>(List.of("0", "0"));

        CtaRoutes routes = new CtaRoutes("2026-07-17T14:06:58", errorCode, null, null);
        errorCode.add("50");

        assertThat(routes.errorCode()).containsExactly("0", "0");
    }

    @Test
    void constructor_copiesErrorMessage_whenNonNull_andAllowsNullElements() {
        List<String> errorMessage = new ArrayList<>(Arrays.asList(null, null));

        CtaRoutes routes = new CtaRoutes("2026-07-17T14:06:58", List.of("0", "0"), errorMessage, null);
        errorMessage.add("late addition");

        assertThat(routes.errorMessage()).containsExactly(null, null);
    }

    @Test
    void constructor_allowsNullErrorCodeAndErrorMessage() {
        CtaRoutes routes = new CtaRoutes("2026-07-17T14:06:58", null, null, null);

        assertThat(routes.errorCode()).isNull();
        assertThat(routes.errorMessage()).isNull();
    }

    @Test
    void constructor_throwsNullPointerException_whenTimestampIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new CtaRoutes(null, null, null, null));
    }
}
