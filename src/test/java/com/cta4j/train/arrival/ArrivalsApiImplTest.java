package com.cta4j.train.arrival;

import com.cta4j.TestFixtures;
import com.cta4j.exception.Cta4jException;
import com.cta4j.train.arrival.internal.impl.ArrivalsApiImpl;
import com.cta4j.train.arrival.query.MapArrivalQuery;
import com.cta4j.train.arrival.query.StopArrivalQuery;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.model.Arrival;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class ArrivalsApiImplTest {
    private WireMockServer server;
    private ArrivalsApiImpl api;

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
        this.api = new ArrivalsApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void findByMapId_returnsArrivals_whenResponseContainsArrivals() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/success.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();
        List<Arrival> arrivals = this.api.findByMapId(query);

        assertThat(arrivals).hasSize(1);
        Arrival arrival = arrivals.getFirst();
        assertThat(arrival.stationId()).isEqualTo("40100");
        assertThat(arrival.stationName()).isEqualTo("Howard");
        assertThat(arrival.destinationName()).isEqualTo("O'Hare");
        assertThat(arrival.delayed()).isFalse();
        assertThat(arrival.approaching()).isFalse();
    }

    @Test
    void findByMapId_returnsEmpty_whenResponseHasNoEta() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/empty.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();
        List<Arrival> arrivals = this.api.findByMapId(query);

        assertThat(arrivals).isEmpty();
    }

    @Test
    void findByMapId_throwsCta4jException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/error.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByMapId_throwsCta4jException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByStopId_returnsArrivals_whenResponseContainsArrivals() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .withQueryParam("stpid", equalTo("30070"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/success.json"))));

        StopArrivalQuery query = StopArrivalQuery.builder("30070").build();
        List<Arrival> arrivals = this.api.findByStopId(query);

        assertThat(arrivals).hasSize(1);
    }

    @Test
    void findByStopId_returnsEmpty_whenResponseHasNoEta() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/empty.json"))));

        StopArrivalQuery query = StopArrivalQuery.builder("30070").build();
        List<Arrival> arrivals = this.api.findByStopId(query);

        assertThat(arrivals).isEmpty();
    }

    @Test
    void findByStopId_throwsCta4jException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/error.json"))));

        StopArrivalQuery query = StopArrivalQuery.builder("30070").build();

        assertThatThrownBy(() -> this.api.findByStopId(query))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByMapId_throwsCta4jException_whenErrCdIsNotNumeric() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/invalid_err_cd.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jException.class);
    }
}
