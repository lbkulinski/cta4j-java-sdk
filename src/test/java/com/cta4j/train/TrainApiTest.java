package com.cta4j.train;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TrainApiTest {
    @Test
    void builder_throwsNullPointerException_whenApiKeyIsNull() {
        assertThatNullPointerException().isThrownBy(() -> TrainApi.builder(null));
    }

    @Test
    void builder_returnsWorkingInstance() {
        TrainApi api = TrainApi.builder("testkey").build();

        assertThat(api).isNotNull();
        assertThat(api.stations()).isNotNull();
    }
}