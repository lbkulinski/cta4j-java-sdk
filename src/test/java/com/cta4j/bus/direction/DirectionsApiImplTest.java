package com.cta4j.bus.direction;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.direction.internal.impl.DirectionsApiImpl;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class DirectionsApiImplTest {
    private WireMockServer server;
    private DirectionsApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new DirectionsApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void findByRouteId_returnsDirections_whenResponseContainsDirections() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdirections"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/direction/success.json"))));

        List<String> directions = this.api.findByRouteId("22");

        assertThat(directions).containsExactly("Eastbound", "Westbound");
    }

    @Test
    void findByRouteId_returnsEmpty_whenResponseHasNoDataAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdirections"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/direction/empty.json"))));

        List<String> directions = this.api.findByRouteId("22");

        assertThat(directions).isEmpty();
    }

    @Test
    void findByRouteId_returnsEmpty_whenAllErrorsAreResourceSpecific() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdirections"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/direction/not_found.json"))));

        List<String> directions = this.api.findByRouteId("X9");

        assertThat(directions).isEmpty();
    }

    @Test
    void findByRouteId_throwsCta4jBusException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdirections"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/direction/error.json"))));

        assertThatThrownBy(() -> this.api.findByRouteId("22"))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Internal server error")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.DIRECTIONS_ENDPOINT));
    }

    @Test
    void findByRouteId_throwsCta4jBusException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdirections"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.findByRouteId("22"))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.DIRECTIONS_ENDPOINT))
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(JacksonException.class));
    }

    @Test
    void findByRouteId_throwsCta4jBusException_whenServerReturnsErrorStatus() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdirections"))
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatThrownBy(() -> this.api.findByRouteId("22"))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessageContaining("status code: 500")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.DIRECTIONS_ENDPOINT))
            .satisfies(e -> assertThat(e.getCause()).isNotNull());
    }
}
