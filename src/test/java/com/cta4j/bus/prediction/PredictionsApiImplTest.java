package com.cta4j.bus.prediction;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.prediction.internal.impl.PredictionsApiImpl;
import com.cta4j.bus.prediction.model.Prediction;
import com.cta4j.bus.prediction.query.StopsPredictionsQuery;
import com.cta4j.bus.prediction.query.VehiclesPredictionsQuery;
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
    void findByStopIds_throwsCta4jBusException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/error.json"))));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("456")).build();

        assertThatThrownBy(() -> this.api.findByStopIds(query))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Internal server error")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.PREDICTIONS_ENDPOINT));
    }

    @Test
    void findByStopIds_throwsCta4jBusException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("456")).build();

        assertThatThrownBy(() -> this.api.findByStopIds(query))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.PREDICTIONS_ENDPOINT));
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

    @Test
    void findByStopIds_sendsRtParameter_whenRouteIdsProvided() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .withQueryParam("rt", equalTo("8"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/success.json"))));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("456"))
            .routeIds(List.of("8"))
            .build();
        List<Prediction> predictions = this.api.findByStopIds(query);

        assertThat(predictions).hasSize(1);
    }

    @Test
    void findByStopIds_sendsTopParameter_whenMaxResultsProvided() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .withQueryParam("top", equalTo("5"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/success.json"))));

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("456"))
            .maxResults(5)
            .build();
        List<Prediction> predictions = this.api.findByStopIds(query);

        assertThat(predictions).hasSize(1);
    }

    @Test
    void findByStopId_stringOverload_returnsPredictions_whenResponseContainsPredictions() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .withQueryParam("stpid", equalTo("456"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/success.json"))));

        List<Prediction> predictions = this.api.findByStopId("456");

        assertThat(predictions).hasSize(1);
    }

    @Test
    void findByRouteIdAndStopId_sendsRtAndStpidParameters() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .withQueryParam("stpid", equalTo("456"))
            .withQueryParam("rt", equalTo("8"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/success.json"))));

        List<Prediction> predictions = this.api.findByRouteIdAndStopId("8", "456");

        assertThat(predictions).hasSize(1);
    }

    @Test
    void findByVehicleId_stringOverload_returnsPredictions_whenResponseContainsPredictions() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .withQueryParam("vid", equalTo("509"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/success.json"))));

        List<Prediction> predictions = this.api.findByVehicleId("509");

        assertThat(predictions).hasSize(1);
    }

    @Test
    void findByVehicleIds_sendsTopParameter_whenMaxResultsProvided() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpredictions"))
            .withQueryParam("top", equalTo("3"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/prediction/success.json"))));

        VehiclesPredictionsQuery query = VehiclesPredictionsQuery.builder(List.of("509"))
            .maxResults(3)
            .build();
        List<Prediction> predictions = this.api.findByVehicleIds(query);

        assertThat(predictions).hasSize(1);
    }
}
