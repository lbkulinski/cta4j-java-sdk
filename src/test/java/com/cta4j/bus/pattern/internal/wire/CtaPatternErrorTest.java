package com.cta4j.bus.pattern.internal.wire;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CtaPatternErrorTest {
    @Test
    void notFound_returnsTrue_whenPidPresent() {
        CtaPatternError error = new CtaPatternError("No data found for pattern", "9999", null, null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsTrue_whenRtPresent() {
        CtaPatternError error = new CtaPatternError("No data found for parameter", null, "9999", null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsFalse_whenNeitherPidNorRtPresent() {
        CtaPatternError error = new CtaPatternError("Invalid API access key supplied", null, null, "cta");

        assertThat(error.notFound()).isFalse();
    }
}
