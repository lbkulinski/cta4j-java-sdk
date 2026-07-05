package com.cta4j.bus.direction.internal.wire;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CtaDirectionErrorTest {
    @Test
    void notFound_returnsTrue_whenRtPresent() {
        CtaDirectionError error = new CtaDirectionError("No data found for parameter", "9999", null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsFalse_whenRtAbsent() {
        CtaDirectionError error = new CtaDirectionError("Invalid API access key supplied", null, "cta");

        assertThat(error.notFound()).isFalse();
    }
}