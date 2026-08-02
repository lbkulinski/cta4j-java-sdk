package com.cta4j.bus.prediction.internal.wire;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CtaPredictionErrorTest {
    @Test
    void notFound_returnsTrue_whenStpidPresent() {
        CtaPredictionError error = new CtaPredictionError("No data found for stop", "99999", null, null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsTrue_whenVidPresent() {
        CtaPredictionError error = new CtaPredictionError("No data found for parameter", null, "9999", null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsFalse_whenNeitherStpidNorVidPresent() {
        CtaPredictionError error = new CtaPredictionError("Invalid API access key supplied", null, null, "cta");

        assertThat(error.notFound()).isFalse();
    }
}
