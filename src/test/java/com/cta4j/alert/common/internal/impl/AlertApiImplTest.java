package com.cta4j.alert.common.internal.impl;

import com.cta4j.alert.AlertApi;
import com.cta4j.alert.common.internal.config.AlertApiConfig;
import com.cta4j.alert.routestatus.RouteStatusApi;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class AlertApiImplTest {
    private WireMockServer server;
    private AlertApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        AlertApiConfig config = new AlertApiConfig("http", "localhost", this.server.port());
        this.api = new AlertApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void constructor_throwsNullPointerException_whenConfigIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new AlertApiImpl(null));
    }

    @Test
    void routeStatus_returnsNonNull() {
        RouteStatusApi result = this.api.routeStatus();

        assertThat(result).isNotNull();
    }

    @Test
    void builderImpl_host_throwsNullPointerException_whenHostIsNull() {
        AlertApiImpl.BuilderImpl builder = new AlertApiImpl.BuilderImpl();

        assertThatNullPointerException().isThrownBy(() -> builder.host(null));
    }

    @Test
    void builderImpl_build_returnsInstance_withDefaultHost() {
        AlertApiImpl.BuilderImpl builder = new AlertApiImpl.BuilderImpl();

        AlertApi result = builder.build();

        assertThat(result).isNotNull();
    }

    @Test
    void builderImpl_build_returnsInstance_withCustomHost() {
        AlertApiImpl.BuilderImpl builder = new AlertApiImpl.BuilderImpl();
        builder.host("example.com");

        AlertApi result = builder.build();

        assertThat(result).isNotNull();
    }
}
