package com.cta4j.bus.vehicle;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.vehicle.internal.impl.VehiclesApiImpl;
import com.cta4j.bus.vehicle.model.Vehicle;
import com.cta4j.exception.Cta4jException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class VehiclesApiImplTest {
    private WireMockServer server;
    private VehiclesApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new VehiclesApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void findByIds_returnsVehicles_whenResponseContainsVehicles() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getvehicles"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/vehicle/success.json"))));

        List<Vehicle> vehicles = this.api.findByIds(List.of("509"));

        assertThat(vehicles).hasSize(1);
        Vehicle vehicle = vehicles.getFirst();
        assertThat(vehicle.id()).isEqualTo("509");
        assertThat(vehicle.routeId()).isEqualTo("8");
        assertThat(vehicle.destination()).isEqualTo("Waveland/Broadway");
        assertThat(vehicle.delayed()).isFalse();
    }

    @Test
    void findByIds_returnsEmpty_whenInputIsEmpty() {
        List<Vehicle> vehicles = this.api.findByIds(List.of());

        assertThat(vehicles).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByIds_returnsEmpty_whenResponseHasNoDataAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getvehicles"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/vehicle/empty.json"))));

        List<Vehicle> vehicles = this.api.findByIds(List.of("509"));

        assertThat(vehicles).isEmpty();
    }

    @Test
    void findByIds_returnsEmpty_whenAllErrorsAreResourceSpecific() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getvehicles"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/vehicle/not_found.json"))));

        List<Vehicle> vehicles = this.api.findByIds(List.of("9999"));

        assertThat(vehicles).isEmpty();
    }

    @Test
    void findByIds_throwsCta4jException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getvehicles"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/vehicle/error.json"))));

        assertThatThrownBy(() -> this.api.findByIds(List.of("509")))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByIds_throwsCta4jException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getvehicles"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.findByIds(List.of("509")))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByRouteIds_returnsEmpty_whenInputIsEmpty() {
        List<Vehicle> vehicles = this.api.findByRouteIds(List.of());

        assertThat(vehicles).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByRouteIds_sendsRtParameter() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getvehicles"))
            .withQueryParam("rt", equalTo("8"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/vehicle/success.json"))));

        List<Vehicle> vehicles = this.api.findByRouteIds(List.of("8"));

        assertThat(vehicles).hasSize(1);
    }
}
