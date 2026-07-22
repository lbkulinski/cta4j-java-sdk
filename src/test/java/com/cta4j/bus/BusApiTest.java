package com.cta4j.bus;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BusApiTest {
    @Test
    void builder_throwsNullPointerException_whenApiKeyIsNull() {
        assertThatNullPointerException().isThrownBy(() -> BusApi.builder(null));
    }

    @Test
    void builder_returnsWorkingInstance() {
        BusApi api = BusApi.builder("testkey").build();

        assertThat(api).isNotNull();
        assertThat(api.vehicles()).isNotNull();
    }
}
