package com.cta4j.bus.vehicle.internal.wire;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CtaVehicleErrorTest {
    @Test
    void notFound_returnsTrue_whenVidPresent() {
        CtaVehicleError error = new CtaVehicleError("No data found for parameter", "9999", null, null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsTrue_whenRtPresent() {
        CtaVehicleError error = new CtaVehicleError("No data found for parameter", null, "9999", null);

        assertThat(error.notFound()).isTrue();
    }

    @Test
    void notFound_returnsFalse_whenNeitherVidNorRtPresent() {
        CtaVehicleError error = new CtaVehicleError("Invalid API access key supplied", null, null, "cta");

        assertThat(error.notFound()).isFalse();
    }
}
