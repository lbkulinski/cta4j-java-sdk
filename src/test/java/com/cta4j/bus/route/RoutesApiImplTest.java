package com.cta4j.bus.route;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.route.internal.impl.RoutesApiImpl;
import com.cta4j.bus.route.model.Route;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

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
    void list_throwsCta4jBusException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getroutes"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/route/error.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Internal server error")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.ROUTES_ENDPOINT));
    }

    @Test
    void list_throwsCta4jBusException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getroutes"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.ROUTES_ENDPOINT))
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(JacksonException.class));
    }

    @Test
    void list_throwsCta4jBusException_whenServerReturnsErrorStatus() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getroutes"))
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jBusException.class)
            .hasMessageContaining("status code: 500")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.ROUTES_ENDPOINT))
            .satisfies(e -> assertThat(e.getCause()).isNotNull());
    }
}
