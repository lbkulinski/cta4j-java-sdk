package com.cta4j.bus.api.prediction.impl;

import com.cta4j.bus.api.common.util.ApiUtils;
import com.cta4j.bus.api.prediction.PredictionsApi;
import com.cta4j.bus.api.prediction.external.CtaPrediction;
import com.cta4j.bus.api.prediction.mapper.PredictionMapper;
import com.cta4j.bus.api.prediction.model.Prediction;
import com.cta4j.bus.api.prediction.query.StopsPredictionsQuery;
import com.cta4j.bus.api.prediction.query.VehiclesPredictionsQuery;
import com.cta4j.bus.api.common.external.CtaBustimeResponse;
import com.cta4j.bus.api.common.external.CtaError;
import com.cta4j.bus.api.common.external.CtaResponse;
import com.cta4j.exception.Cta4jException;
import com.cta4j.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class PredictionsApiImpl implements PredictionsApi {
    private static final String PREDICTIONS_ENDPOINT = String.format("%s/getpredictions", ApiUtils.API_PREFIX);
    private static final int MAX_STOP_IDS_PER_REQUEST = 10;
    private static final int MAX_VEHICLE_IDS_PER_REQUEST = 10;

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public PredictionsApiImpl(
        String host,
        String apiKey,
        ObjectMapper objectMapper
    ) {
        this.host = Objects.requireNonNull(host);
        this.apiKey = Objects.requireNonNull(apiKey);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public List<Prediction> findByStopIds(StopsPredictionsQuery query) {
        Objects.requireNonNull(query);

        List<String> stopIds = query.stopIds();

        if (stopIds.size() > MAX_STOP_IDS_PER_REQUEST) {
            String message = String.format(
                "A maximum of %d stop IDs can be requested at once, but %d were provided",
                MAX_STOP_IDS_PER_REQUEST,
                stopIds.size()
            );

            throw new IllegalArgumentException(message);
        }

        String stopIdsString = String.join(",", stopIds);

        URIBuilder builder = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("stpid", stopIdsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.apiKey)
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

        if (vehicleIds.size() > MAX_VEHICLE_IDS_PER_REQUEST) {
            String message = String.format(
                "A maximum of %d vehicle IDs can be requested at once, but %d were provided",
                MAX_STOP_IDS_PER_REQUEST,
                vehicleIds.size()
            );

            throw new IllegalArgumentException(message);
        }

        String vehicleIdsString = String.join(",", vehicleIds);

        URIBuilder builder = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("vid", vehicleIdsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.apiKey)
            .addParameter("format", "json");

        if (query.maxResults() != null) {
            String maxResultsString = String.valueOf(query.maxResults());

            builder.addParameter("top", maxResultsString);
        }

        String url = builder.toString();

        return this.makeRequest(url);
    }

    private List<Prediction> makeRequest(String url) {
        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaPrediction>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaPrediction>> predictionsResponse;

        try {
            predictionsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", PREDICTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaPrediction>> bustimeResponse = predictionsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaPrediction> predictions = bustimeResponse.data();

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(PREDICTIONS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((predictions == null) || predictions.isEmpty()) {
            return List.of();
        }

        return predictions.stream()
                          .map(PredictionMapper.MAPPER::toDomain)
                          .toList();
    }
}
