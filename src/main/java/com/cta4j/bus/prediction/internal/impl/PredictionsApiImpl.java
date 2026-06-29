package com.cta4j.bus.prediction.internal.impl;

import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.prediction.PredictionsApi;
import com.cta4j.bus.prediction.internal.wire.CtaPrediction;
import com.cta4j.bus.prediction.internal.mapper.PredictionMapper;
import com.cta4j.bus.prediction.internal.wire.CtaPredictionBustimeResponse;
import com.cta4j.bus.prediction.internal.wire.CtaPredictionError;
import com.cta4j.bus.prediction.model.Prediction;
import com.cta4j.bus.prediction.query.StopsPredictionsQuery;
import com.cta4j.bus.prediction.query.VehiclesPredictionsQuery;
import com.cta4j.exception.Cta4jException;
import com.cta4j.common.internal.http.HttpClient;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class PredictionsApiImpl implements PredictionsApi {
    private static final Logger log = LoggerFactory.getLogger(PredictionsApiImpl.class);

    private static final String PREDICTIONS_ENDPOINT = "%s/getpredictions".formatted(ApiUtils.API_PREFIX);

    private final BusApiConfig config;

    public PredictionsApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Prediction> findByStopIds(StopsPredictionsQuery query) {
        Objects.requireNonNull(query);

        List<String> stopIds = query.stopIds();

        if (stopIds.isEmpty())  {
            return List.of();
        }

        String stopIdsString = String.join(",", stopIds);

        URIBuilder builder = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("stpid", stopIdsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json");

        if (query.routeIds() != null) {
            String routeIdsString = String.join(",", query.routeIds());

            builder.addParameter("rt", routeIdsString);
        }

        if (query.maxResults() != null) {
            String maxResultsString = String.valueOf(query.maxResults());

            builder.addParameter("top", maxResultsString);
        }

        String url = builder.toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Prediction> findByVehicleIds(VehiclesPredictionsQuery query) {
        Objects.requireNonNull(query);

        List<String> vehicleIds = query.vehicleIds();

        if (vehicleIds.isEmpty()) {
            return List.of();
        }

        String vehicleIdsString = String.join(",", vehicleIds);

        URIBuilder builder = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("vid", vehicleIdsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json");

        if (query.maxResults() != null) {
            String maxResultsString = String.valueOf(query.maxResults());

            builder.addParameter("top", maxResultsString);
        }

        String url = builder.toString();

        return this.makeRequest(url);
    }

    private List<Prediction> makeRequest(String url) {
        String response = HttpClient.get(url);

        TypeReference<CtaResponse<CtaPredictionBustimeResponse>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaPredictionBustimeResponse> predictionsResponse;

        try {
            predictionsResponse = JsonMapper.shared()
                                            .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(PREDICTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaPredictionBustimeResponse bustimeResponse = predictionsResponse.bustimeResponse();

        List<CtaPrediction> predictions = bustimeResponse.prd();
        List<CtaPredictionError> errors = bustimeResponse.error();

        if (predictions != null && !predictions.isEmpty()) {
            return predictions.stream()
                              .map(PredictionMapper.INSTANCE::toDomain)
                              .toList();
        }

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", PREDICTIONS_ENDPOINT);

            return List.of();
        }

        boolean notFound = errors.stream()
                                 .allMatch(error -> error.stpid() != null || error.vid() != null);

        if (notFound) {
            return List.of();
        }

        String message = ApiUtils.buildErrorMessage(PREDICTIONS_ENDPOINT, errors);

        throw new Cta4jException(message);
    }
}
