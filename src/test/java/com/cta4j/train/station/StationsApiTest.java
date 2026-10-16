package com.cta4j.train.station;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StationsApiTest {
    @Test
    void builder_returnsWorkingInstance() {
        StationsApi api = StationsApi.builder().build();

        assertThat(api).isNotNull();
    }
}
