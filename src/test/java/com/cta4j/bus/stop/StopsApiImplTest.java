package com.cta4j.bus.stop;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.stop.internal.impl.StopsApiImpl;
import com.cta4j.bus.stop.model.Stop;
import com.cta4j.exception.Cta4jException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class StopsApiImplTest {
    private WireMockServer server;
    private StopsApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new StopsApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void findByRouteIdAndDirection_returnsStops_whenResponseContainsStops() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getstops"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/stop/success.json"))));

        List<Stop> stops = this.api.findByRouteIdAndDirection("8", "Northbound");

        assertThat(stops).hasSize(1);
        Stop stop = stops.getFirst();
        assertThat(stop.id()).isEqualTo("456");
        assertThat(stop.name()).isEqualTo("Ashland & Division");
    }

    @Test
    void findByRouteIdAndDirection_returnsEmpty_whenResponseHasNoDataAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getstops"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/stop/empty.json"))));

        List<Stop> stops = this.api.findByRouteIdAndDirection("8", "Northbound");

        assertThat(stops).isEmpty();
    }

    @Test
    void findByRouteIdAndDirection_returnsEmpty_whenAllErrorsAreResourceSpecific() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getstops"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/stop/not_found.json"))));

        List<Stop> stops = this.api.findByRouteIdAndDirection("8", "Northbound");

        assertThat(stops).isEmpty();
    }

    @Test
    void findByRouteIdAndDirection_throwsCta4jException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getstops"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/stop/error.json"))));

        assertThatThrownBy(() -> this.api.findByRouteIdAndDirection("8", "Northbound"))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByRouteIdAndDirection_throwsCta4jException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getstops"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.findByRouteIdAndDirection("8", "Northbound"))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByIds_returnsEmpty_whenInputIsEmpty() {
        List<Stop> stops = this.api.findByIds(List.of());

        assertThat(stops).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByIds_sendsStpidParameter() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getstops"))
            .withQueryParam("stpid", equalTo("456"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/stop/success.json"))));

        List<Stop> stops = this.api.findByIds(List.of("456"));

        assertThat(stops).hasSize(1);
    }
}
