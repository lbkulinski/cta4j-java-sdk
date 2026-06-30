package com.cta4j.train.common.internal.impl;

import com.cta4j.train.TrainApi;
import com.cta4j.train.arrival.ArrivalsApi;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.follow.FollowApi;
import com.cta4j.train.location.LocationsApi;
import com.cta4j.train.station.StationsApi;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class TrainApiImplTest {
    private WireMockServer server;
    private TrainApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        TrainApiConfig config = new TrainApiConfig(
            "http",
            "localhost",
            this.server.port(),
            "http://localhost:" + this.server.port() + "/stations",
            "testkey"
        );
        this.api = new TrainApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void constructor_throwsNullPointerException_whenConfigIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new TrainApiImpl(null));
    }

    @Test
    void stations_returnsNonNull() {
        StationsApi result = this.api.stations();

        assertThat(result).isNotNull();
    }

    @Test
    void arrivals_returnsNonNull() {
        ArrivalsApi result = this.api.arrivals();

        assertThat(result).isNotNull();
    }

    @Test
    void follow_returnsNonNull() {
        FollowApi result = this.api.follow();

        assertThat(result).isNotNull();
    }

    @Test
    void locations_returnsNonNull() {
        LocationsApi result = this.api.locations();

        assertThat(result).isNotNull();
    }

    @Test
    void builderImpl_constructor_throwsNullPointerException_whenApiKeyIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new TrainApiImpl.BuilderImpl(null));
    }

    @Test
    void builderImpl_host_throwsNullPointerException_whenHostIsNull() {
        TrainApiImpl.BuilderImpl builder = new TrainApiImpl.BuilderImpl("testkey");

        assertThatNullPointerException().isThrownBy(() -> builder.host(null));
    }

    @Test
    void builderImpl_stationsUrl_throwsNullPointerException_whenUrlIsNull() {
        TrainApiImpl.BuilderImpl builder = new TrainApiImpl.BuilderImpl("testkey");

        assertThatNullPointerException().isThrownBy(() -> builder.stationsUrl(null));
    }

    @Test
    void builderImpl_build_returnsInstance_withDefaultHostAndStationsUrl() {
        TrainApiImpl.BuilderImpl builder = new TrainApiImpl.BuilderImpl("testkey");

        TrainApi result = builder.build();

        assertThat(result).isNotNull();
    }

    @Test
    void builderImpl_build_returnsInstance_withCustomHost() {
        TrainApiImpl.BuilderImpl builder = new TrainApiImpl.BuilderImpl("testkey");
        builder.host("example.com");

        TrainApi result = builder.build();

        assertThat(result).isNotNull();
    }

    @Test
    void builderImpl_build_returnsInstance_withCustomStationsUrl() {
        TrainApiImpl.BuilderImpl builder = new TrainApiImpl.BuilderImpl("testkey");
        builder.stationsUrl("https://example.com/stations");

        TrainApi result = builder.build();

        assertThat(result).isNotNull();
    }
}
