package com.cta4j.bus.route;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.route.internal.impl.RoutesApiImpl;
import com.cta4j.bus.route.model.Route;
import com.cta4j.exception.Cta4jException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class RoutesApiImplTest {
    private WireMockServer server;
    private RoutesApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new RoutesApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void list_returnsRoutes_whenResponseContainsRoutes() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getroutes"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/route/success.json"))));

        List<Route> routes = this.api.list();

        assertThat(routes).hasSize(1);
        Route route = routes.getFirst();
        assertThat(route.id()).isEqualTo("22");
        assertThat(route.name()).isEqualTo("Clark");
        assertThat(route.color()).isEqualTo("#ffffff");
        assertThat(route.designator()).isEqualTo("22");
        assertThat(route.dataFeed()).isNull();
    }

    @Test
    void list_returnsEmpty_whenResponseHasNoDataAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getroutes"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/route/empty.json"))));

        List<Route> routes = this.api.list();

        assertThat(routes).isEmpty();
    }

    @Test
    void list_throwsCta4jException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getroutes"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/route/error.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void list_throwsCta4jException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getroutes"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jException.class);
    }
}
