package com.cta4j.bus.common.internal.impl;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.BusApiConstants;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class SystemTimeApiImplTest {
    private WireMockServer server;
    private SystemTimeApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new SystemTimeApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
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
    void systemTime_throwsCta4jBusException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/gettime"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/time/error.json"))));

        assertThatThrownBy(() -> this.api.systemTime())
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Invalid API key")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.SYSTEM_TIME_ENDPOINT));
    }

    @Test
    void systemTime_throwsCta4jBusException_whenResponseHasNoTimeAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/gettime"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/time/empty.json"))));

        assertThatThrownBy(() -> this.api.systemTime())
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("No system time data returned")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.SYSTEM_TIME_ENDPOINT));
    }

    @Test
    void systemTime_throwsCta4jBusException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/gettime"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.systemTime())
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.SYSTEM_TIME_ENDPOINT));
    }
}