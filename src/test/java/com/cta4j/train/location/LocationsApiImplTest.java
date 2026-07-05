package com.cta4j.train.location;

import com.cta4j.TestFixtures;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.location.exception.Cta4jLocationsException;
import com.cta4j.train.location.exception.LocationsErrorCode;
import com.cta4j.train.location.internal.impl.LocationsApiImpl;
import com.cta4j.train.location.model.TrainLocations;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class LocationsApiImplTest {
    private WireMockServer server;
    private LocationsApiImpl api;

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
        this.api = new LocationsApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void findByLines_returnsLocations_whenResponseContainsData() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/location/success.json"))));

        List<TrainLocations> locations = this.api.findByLines(List.of(TrainLine.RED));

        assertThat(locations).hasSize(1);
        TrainLocations redLine = locations.getFirst();
        assertThat(redLine.line()).isEqualTo(TrainLine.RED);
        assertThat(redLine.trains()).hasSize(1);
    }

    @Test
    void findByLines_returnsEmpty_whenInputIsEmpty() {
        List<TrainLocations> locations = this.api.findByLines(List.of());

        assertThat(locations).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByLines_returnsEmpty_whenResponseHasNoRoute() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/location/empty.json"))));

        List<TrainLocations> locations = this.api.findByLines(List.of(TrainLine.RED));

        assertThat(locations).isEmpty();
    }

    @Test
    void findByLines_throwsCta4jLocationsException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/location/error.json"))));

        assertThatThrownBy(() -> this.api.findByLines(List.of(TrainLine.RED)))
            .isInstanceOf(Cta4jLocationsException.class)
            .hasMessage("Invalid API key")
            .satisfies(e -> assertThat(((Cta4jLocationsException) e).getErrorCode())
                .isEqualTo(LocationsErrorCode.UNKNOWN))
            .satisfies(e -> assertThat(((Cta4jLocationsException) e).getRawErrorCode()).isEqualTo(1));
    }

    @Test
    void findByLines_throwsCta4jLocationsException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.findByLines(List.of(TrainLine.RED)))
            .isInstanceOf(Cta4jLocationsException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jLocationsException) e).getErrorCode()).isNull())
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(JacksonException.class));
    }

    @Test
    void findByLines_sendsRtParameter_asCommaJoined() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .withQueryParam("rt", equalTo("Red,Blue"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/location/success.json"))));

        List<TrainLocations> locations = this.api.findByLines(List.of(TrainLine.RED, TrainLine.BLUE));

        assertThat(locations).hasSize(1);
    }

    @Test
    void findByLine_sendsRtParameter() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .withQueryParam("rt", equalTo("Red"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/location/success.json"))));

        List<TrainLocations> locations = this.api.findByLine(TrainLine.RED);

        assertThat(locations).hasSize(1);
    }

    @Test
    void findAll_returnsLocations_whenResponseContainsData() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/location/success.json"))));

        List<TrainLocations> locations = this.api.findAll();

        assertThat(locations).hasSize(1);
    }

    @Test
    void findByLines_throwsCta4jLocationsException_whenErrCdIsNotNumeric() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/location/invalid_err_cd.json"))));

        assertThatThrownBy(() -> this.api.findByLines(List.of(TrainLine.RED)))
            .isInstanceOf(Cta4jLocationsException.class)
            .hasMessage("Failed to parse error code")
            .satisfies(e -> assertThat(((Cta4jLocationsException) e).getErrorCode()).isNull())
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(NumberFormatException.class));
    }

    @Test
    void findByLines_throwsCta4jLocationsException_whenErrCdIsNegative() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"ctatt\":{\"tmst\":\"2015-04-30T20:23:53\",\"errCd\":\"-1\",\"errNm\":\"Unexpected error\"}}")));

        assertThatThrownBy(() -> this.api.findByLines(List.of(TrainLine.RED)))
            .isInstanceOf(Cta4jLocationsException.class)
            .hasMessage("Unknown error code")
            .satisfies(e -> assertThat(((Cta4jLocationsException) e).getErrorCode())
                .isEqualTo(LocationsErrorCode.UNKNOWN))
            .satisfies(e -> assertThat(((Cta4jLocationsException) e).getRawErrorCode()).isEqualTo(-1));
    }

    @Test
    void findByLines_throwsCta4jLocationsException_whenServerReturnsErrorStatus() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttpositions.aspx"))
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatThrownBy(() -> this.api.findByLines(List.of(TrainLine.RED)))
            .isInstanceOf(Cta4jLocationsException.class)
            .hasMessageContaining("status code: 500")
            .satisfies(e -> assertThat(e.getCause()).isNotNull());
    }
}
