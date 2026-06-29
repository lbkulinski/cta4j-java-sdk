package com.cta4j.bus.prediction;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.prediction.internal.impl.PredictionsApiImpl;
import com.cta4j.bus.prediction.model.Prediction;
import com.cta4j.bus.prediction.query.StopsPredictionsQuery;
import com.cta4j.bus.prediction.query.VehiclesPredictionsQuery;
import com.cta4j.exception.Cta4jException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class PredictionsApiImplTest {
    private WireMockServer server;
    private PredictionsApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new PredictionsApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void findByStopIds_returnsPredictions_whenResponseContainsPredictions() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/success.json"))));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("456")).build();
        List<Prediction> predictions = this.api.findByStopIds(query);

        assertThat(predictions).hasSize(1);
        Prediction prediction = predictions.getFirst();
        assertThat(prediction.stopId()).isEqualTo("456");
        assertThat(prediction.routeId()).isEqualTo("8");
        assertThat(prediction.destination()).isEqualTo("Waveland/Broadway");
    }

    @Test
    void findByStopIds_returnsEmpty_whenInputIsEmpty() {
        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of()).build();
        List<Prediction> predictions = this.api.findByStopIds(query);

        assertThat(predictions).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByStopIds_returnsEmpty_whenResponseHasNoDataAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/empty.json"))));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("456")).build();
        List<Prediction> predictions = this.api.findByStopIds(query);

        assertThat(predictions).isEmpty();
    }

    @Test
    void findByStopIds_returnsEmpty_whenAllErrorsAreResourceSpecific() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/not_found.json"))));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("99999")).build();
        List<Prediction> predictions = this.api.findByStopIds(query);

        assertThat(predictions).isEmpty();
    }

    @Test
    void findByStopIds_throwsCta4jException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/error.json"))));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("456")).build();

        assertThatThrownBy(() -> this.api.findByStopIds(query))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByStopIds_throwsCta4jException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("456")).build();

        assertThatThrownBy(() -> this.api.findByStopIds(query))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByVehicleIds_returnsEmpty_whenInputIsEmpty() {
        VehiclesPredictionsQuery query = VehiclesPredictionsQuery.builder(List.of()).build();
        List<Prediction> predictions = this.api.findByVehicleIds(query);

        assertThat(predictions).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByVehicleIds_sendsVidParameter() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .withQueryParam("vid", equalTo("509"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/success.json"))));

        VehiclesPredictionsQuery query = VehiclesPredictionsQuery.builder(List.of("509")).build();
        List<Prediction> predictions = this.api.findByVehicleIds(query);

        assertThat(predictions).hasSize(1);
    }
}
