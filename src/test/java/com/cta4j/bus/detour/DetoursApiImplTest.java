package com.cta4j.bus.detour;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.detour.internal.impl.DetoursApiImpl;
import com.cta4j.bus.detour.model.Detour;
import com.cta4j.common.exception.Cta4jException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class DetoursApiImplTest {
    private WireMockServer server;
    private DetoursApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new DetoursApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void list_returnsDetours_whenResponseContainsDetours() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdetours"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/detour/success.json"))));

        List<Detour> detours = this.api.list();

        assertThat(detours).hasSize(1);
        Detour detour = detours.getFirst();
        assertThat(detour.id()).isEqualTo("1001");
        assertThat(detour.active()).isTrue();
        assertThat(detour.routeDirections()).hasSize(1);
        assertThat(detour.routeDirections().getFirst().routeId()).isEqualTo("8");
    }

    @Test
    void list_returnsEmpty_whenResponseHasNoDataAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdetours"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/detour/empty.json"))));

        List<Detour> detours = this.api.list();

        assertThat(detours).isEmpty();
    }

    @Test
    void list_throwsCta4jException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdetours"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/detour/error.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void list_throwsCta4jException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdetours"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByRouteId_sendsRtParameter() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdetours"))
            .withQueryParam("rt", equalTo("8"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/detour/success.json"))));

        List<Detour> detours = this.api.findByRouteId("8");

        assertThat(detours).hasSize(1);
    }

    @Test
    void findByRouteId_returnsEmpty_whenResponseContainsNotFoundError() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdetours"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/detour/not_found.json"))));

        List<Detour> detours = this.api.findByRouteId("8");

        assertThat(detours).isEmpty();
    }

    @Test
    void findByRouteIdAndDirection_returnsDetours_whenResponseContainsDetours() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdetours"))
            .withQueryParam("rt", equalTo("8"))
            .withQueryParam("rtdir", equalTo("Northbound"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/detour/success.json"))));

        List<Detour> detours = this.api.findByRouteIdAndDirection("8", "Northbound");

        assertThat(detours).hasSize(1);
    }

    @Test
    void findByRouteIdAndDirection_returnsEmpty_whenResponseContainsNotFoundError() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getdetours"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/detour/not_found.json"))));

        List<Detour> detours = this.api.findByRouteIdAndDirection("8", "Northbound");

        assertThat(detours).isEmpty();
    }
}
