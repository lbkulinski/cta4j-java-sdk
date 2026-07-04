package com.cta4j.bus.detour.internal.wire;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CtaDetourErrorTest {
    @Test
    void notFound_returnsTrue_whenRtPresent() {
        CtaDetourError error = new CtaDetourError("No data found for parameter", "9999", null, null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsFalse_whenRtAbsent() {
        CtaDetourError error = new CtaDetourError("Invalid API access key supplied", null, null, "cta");

        assertThat(error.notFound()).isFalse();
    }
}