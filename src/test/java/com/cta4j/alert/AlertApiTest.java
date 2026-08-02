package com.cta4j.alert;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AlertApiTest {
    @Test
    void builder_returnsWorkingInstance() {
        AlertApi api = AlertApi.builder().build();

        assertThat(api).isNotNull();
        assertThat(api.routeStatus()).isNotNull();
    }
}
