package com.cta4j.bus.common.internal.impl;

import com.cta4j.TestFixtures;
import com.cta4j.bus.BusApi;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.detour.DetoursApi;
import com.cta4j.bus.direction.DirectionsApi;
import com.cta4j.bus.locale.LocalesApi;
import com.cta4j.bus.pattern.PatternsApi;
import com.cta4j.bus.prediction.PredictionsApi;
import com.cta4j.bus.route.RoutesApi;
import com.cta4j.bus.stop.StopsApi;
import com.cta4j.bus.vehicle.VehiclesApi;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class BusApiImplTest {
    private WireMockServer server;
    private BusApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new BusApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void constructor_throwsNullPointerException_whenConfigIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new BusApiImpl(null));
    }

    @Test
    void vehicles_returnsNonNull() {
        VehiclesApi result = this.api.vehicles();

        assertThat(result).isNotNull();
    }

    @Test
    void routes_returnsNonNull() {
        RoutesApi result = this.api.routes();

        assertThat(result).isNotNull();
    }

    @Test
    void directions_returnsNonNull() {
        DirectionsApi result = this.api.directions();

        assertThat(result).isNotNull();
    }

    @Test
    void stops_returnsNonNull() {
        StopsApi result = this.api.stops();

        assertThat(result).isNotNull();
    }

    @Test
    void patterns_returnsNonNull() {
        PatternsApi result = this.api.patterns();

        assertThat(result).isNotNull();
    }

    @Test
    void predictions_returnsNonNull() {
        PredictionsApi result = this.api.predictions();

        assertThat(result).isNotNull();
    }

    @Test
    void locales_returnsNonNull() {
        LocalesApi result = this.api.locales();

        assertThat(result).isNotNull();
    }

    @Test
    void detours_returnsNonNull() {
        DetoursApi result = this.api.detours();

        assertThat(result).isNotNull();
    }

    @Test
    void systemTime_returnsInstant_whenResponseContainsTime() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/gettime"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/time/success.json"))));

        Instant result = this.api.systemTime();

        assertThat(result).isNotNull();
    }

    @Test
    void builderImpl_constructor_throwsNullPointerException_whenApiKeyIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new BusApiImpl.BuilderImpl(null));
    }

    @Test
    void builderImpl_host_throwsNullPointerException_whenHostIsNull() {
        BusApiImpl.BuilderImpl builder = new BusApiImpl.BuilderImpl("testkey");

        assertThatNullPointerException().isThrownBy(() -> builder.host(null));
    }

    @Test
    void builderImpl_build_returnsInstance_withDefaultHost() {
        BusApiImpl.BuilderImpl builder = new BusApiImpl.BuilderImpl("testkey");

        BusApi result = builder.build();

        assertThat(result).isNotNull();
    }

    @Test
    void builderImpl_build_returnsInstance_withCustomHost() {
        BusApiImpl.BuilderImpl builder = new BusApiImpl.BuilderImpl("testkey");
        builder.host("example.com");

        BusApi result = builder.build();

        assertThat(result).isNotNull();
    }
}