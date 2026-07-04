package com.cta4j.bus.stop.internal.wire;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CtaStopErrorTest {
    @Test
    void notFound_returnsTrue_whenRtAndDirPresent() {
        CtaStopError error = new CtaStopError("No data found for parameter", "9", "1", null, null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsTrue_whenStpidPresent() {
        CtaStopError error = new CtaStopError("No data found for stop", null, null, "99999", null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsFalse_whenRtPresentWithoutDir() {
        CtaStopError error = new CtaStopError("No data found for parameter", "9", null, null, null);

        assertThat(error.notFound()).isFalse();
    }

    @Test
    void notFound_returnsFalse_whenNoTypedFieldsPresent() {
        CtaStopError error = new CtaStopError("Invalid API access key supplied", null, null, null, "cta");

        assertThat(error.notFound()).isFalse();
    }
}