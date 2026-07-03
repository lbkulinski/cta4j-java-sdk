package com.cta4j.train.station;

import com.cta4j.TestFixtures;
import com.cta4j.train.common.exception.Cta4jTrainException;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.station.internal.impl.StationsApiImpl;
import com.cta4j.train.station.model.Station;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class StationsApiImplTest {
    private static final String STATIONS_PATH = "/stations";
    private WireMockServer server;
    private StationsApiImpl api;
    private String stationsUrl;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        this.stationsUrl = "http://localhost:" + this.server.port() + STATIONS_PATH;
        TrainApiConfig config = new TrainApiConfig(
            "http",
            "localhost",
            this.server.port(),
            this.stationsUrl,
            "testkey"
        );
        this.api = new StationsApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void list_returnsStations_whenResponseContainsStations() {
        this.server.stubFor(get(urlPathEqualTo(STATIONS_PATH))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/station/success.json"))));

        List<Station> stations = this.api.list();

        assertThat(stations).hasSize(1);
        Station station = stations.getFirst();
        assertThat(station.stopId()).isEqualTo("30070");
        assertThat(station.name()).isEqualTo("Howard");
        assertThat(station.adaAccessible()).isTrue();
        assertThat(station.lines()).contains(TrainLine.RED);
        assertThat(station.lines()).doesNotContain(TrainLine.BLUE);
    }

    @Test
    void list_returnsEmpty_whenResponseIsEmptyArray() {
        this.server.stubFor(get(urlPathEqualTo(STATIONS_PATH))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/station/empty.json"))));

        List<Station> stations = this.api.list();

        assertThat(stations).isEmpty();
    }

    @Test
    void list_throwsCta4jTrainException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo(STATIONS_PATH))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jTrainException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jTrainException) e).getEndpoint())
                .isEqualTo(this.stationsUrl));
    }
}
