package com.cta4j.train.arrival;

import com.cta4j.TestFixtures;
import com.cta4j.train.arrival.exception.ArrivalsErrorCode;
import com.cta4j.train.arrival.exception.Cta4jArrivalsException;
import com.cta4j.train.arrival.internal.impl.ArrivalsApiImpl;
import com.cta4j.train.arrival.query.MapArrivalQuery;
import com.cta4j.train.arrival.query.StopArrivalQuery;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.model.Arrival;
import com.cta4j.train.common.model.TrainLine;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

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
    void findByMapId_returnsEmpty_whenEtaIsEmptyArray() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"ctatt\":{\"tmst\":\"2015-04-30T20:23:53\",\"errCd\":\"0\",\"errNm\":null,\"eta\":[]}}")));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();
        List<Arrival> arrivals = this.api.findByMapId(query);

        assertThat(arrivals).isEmpty();
    }

    @Test
    void findByMapId_throwsCta4jArrivalsException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/error.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jArrivalsException.class)
            .hasMessage("Invalid API key")
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getErrorCode())
                .isEqualTo(ArrivalsErrorCode.UNKNOWN))
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getRawErrorCode()).isEqualTo(1));
    }

    @Test
    void findByMapId_returnsEmpty_whenResponseContainsInvalidMapIdError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/not_found_mapid.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("99999").build();
        List<Arrival> arrivals = this.api.findByMapId(query);

        assertThat(arrivals).isEmpty();
    }

    @Test
    void findByMapId_throwsCta4jArrivalsException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jArrivalsException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getErrorCode()).isNull())
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(JacksonException.class));
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
    void findByStopId_throwsCta4jArrivalsException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/error.json"))));

        StopArrivalQuery query = StopArrivalQuery.builder("30070").build();

        assertThatThrownBy(() -> this.api.findByStopId(query))
            .isInstanceOf(Cta4jArrivalsException.class)
            .hasMessage("Invalid API key")
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getErrorCode())
                .isEqualTo(ArrivalsErrorCode.UNKNOWN))
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getRawErrorCode()).isEqualTo(1));
    }

    @Test
    void findByStopId_returnsEmpty_whenResponseContainsInvalidStopIdError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/not_found_stpid.json"))));

        StopArrivalQuery query = StopArrivalQuery.builder("99999").build();
        List<Arrival> arrivals = this.api.findByStopId(query);

        assertThat(arrivals).isEmpty();
    }

    @Test
    void findByMapId_sendsLineAndMaxResultsQueryParams_whenSet() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .withQueryParam("rt", equalTo("Red"))
            .withQueryParam("max", equalTo("5"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/success.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900")
            .line(TrainLine.RED)
            .maxResults(5)
            .build();

        List<Arrival> arrivals = this.api.findByMapId(query);

        assertThat(arrivals).hasSize(1);
    }

    @Test
    void findByStopId_sendsLineAndMaxResultsQueryParams_whenSet() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .withQueryParam("stpid", equalTo("30070"))
            .withQueryParam("rt", equalTo("Red"))
            .withQueryParam("max", equalTo("5"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/success.json"))));

        StopArrivalQuery query = StopArrivalQuery.builder("30070")
            .line(TrainLine.RED)
            .maxResults(5)
            .build();

        List<Arrival> arrivals = this.api.findByStopId(query);

        assertThat(arrivals).hasSize(1);
    }

    @Test
    void findByMapId_stringOverload_returnsArrivals_whenResponseContainsArrivals() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/success.json"))));

        List<Arrival> arrivals = this.api.findByMapId("40900");

        assertThat(arrivals).hasSize(1);
    }

    @Test
    void findByStopId_stringOverload_returnsArrivals_whenResponseContainsArrivals() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .withQueryParam("stpid", equalTo("30070"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/success.json"))));

        List<Arrival> arrivals = this.api.findByStopId("30070");

        assertThat(arrivals).hasSize(1);
    }

    @Test
    void findByMapId_throwsCta4jArrivalsException_whenErrCdIsNotNumeric() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/invalid_err_cd.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jArrivalsException.class)
            .hasMessage("Failed to parse error code")
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getErrorCode()).isNull())
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(NumberFormatException.class));
    }

    @Test
    void findByMapId_throwsCta4jArrivalsException_whenErrCdIsNegative() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"ctatt\":{\"tmst\":\"2015-04-30T20:23:53\",\"errCd\":\"-1\",\"errNm\":\"Unexpected error\"}}")));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jArrivalsException.class)
            .hasMessage("Unknown error code")
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getErrorCode())
                .isEqualTo(ArrivalsErrorCode.UNKNOWN))
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getRawErrorCode()).isEqualTo(-1));
    }

    @Test
    void findByMapId_throwsCta4jArrivalsException_withDefaultMessage_whenErrNmIsBlank() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"ctatt\":{\"tmst\":\"2015-04-30T20:23:53\",\"errCd\":\"1\",\"errNm\":\"\"}}")));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jArrivalsException.class)
            .hasMessage("An unknown error occurred.")
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getErrorCode())
                .isEqualTo(ArrivalsErrorCode.UNKNOWN))
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).getRawErrorCode()).isEqualTo(1));
    }

    @Test
    void findByMapId_throwsCta4jArrivalsException_whenServerReturnsErrorStatus() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(500)));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jArrivalsException.class)
            .hasMessageContaining("status code: 500")
            .satisfies(e -> assertThat(e.getCause()).isNotNull());
    }
}
