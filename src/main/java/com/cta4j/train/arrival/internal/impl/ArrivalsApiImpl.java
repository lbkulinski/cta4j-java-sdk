package com.cta4j.train.arrival.internal.impl;

import com.cta4j.exception.Cta4jException;
import com.cta4j.common.internal.http.HttpClient;
import com.cta4j.train.arrival.ArrivalsApi;
import com.cta4j.train.common.internal.mapper.ArrivalMapper;
import com.cta4j.train.arrival.internal.wire.CtaArrivalsResponse;
import com.cta4j.train.common.model.Arrival;
import com.cta4j.train.arrival.query.MapArrivalQuery;
import com.cta4j.train.arrival.query.StopArrivalQuery;
import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.internal.util.ApiUtils;
import com.cta4j.train.common.internal.wire.CtaArrival;
import com.cta4j.train.common.internal.wire.CtaError;
import com.cta4j.train.common.internal.wire.CtaResponse;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ArrivalsApiImpl implements ArrivalsApi {
    private static final String ARRIVALS_ENDPOINT = "%s/ttarrivals.aspx".formatted(ApiUtils.API_PREFIX);
    private static final TypeReference<CtaResponse<CtaArrivalsResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    private final TrainApiConfig config;

    public ArrivalsApiImpl(TrainApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Arrival> findByMapId(MapArrivalQuery query) {
        Objects.requireNonNull(query);

        URIBuilder builder = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(ARRIVALS_ENDPOINT)
            .addParameter("mapid", query.mapId())
            .addParameter("key", this.config.apiKey())
            .addParameter("outputType", "JSON");

        return this.makeRequest(builder, query.line(), query.maxResults());
    }

    @Override
    public List<Arrival> findByStopId(StopArrivalQuery query) {
        Objects.requireNonNull(query);

        URIBuilder builder = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(ARRIVALS_ENDPOINT)
            .addParameter("stpid", query.stopId())
            .addParameter("key", this.config.apiKey())
            .addParameter("outputType", "JSON");

        return this.makeRequest(builder, query.line(), query.maxResults());
    }

    private List<Arrival> makeRequest(
        URIBuilder builder,
        @Nullable TrainLine line,
        @Nullable Integer maxResults
    ) {
        if (line != null) {
            builder.addParameter("rt", line.getCode());
        }

        if (maxResults != null) {
            builder.addParameter("max", maxResults.toString());
        }

        String url = builder.toString();

        String response = HttpClient.get(url);

        CtaResponse<CtaArrivalsResponse> ctaResponse;

        try {
            ctaResponse = JsonMapper.shared()
                                    .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(ARRIVALS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaArrivalsResponse arrivalsResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(arrivalsResponse.errCd(), ARRIVALS_ENDPOINT);

        if (errCd != 0) {
            CtaError error = new CtaError(errCd, arrivalsResponse.errNm());

            String message = ApiUtils.buildErrorMessage(ARRIVALS_ENDPOINT, error);

            throw new Cta4jException(message);
        }

        List<CtaArrival> eta = arrivalsResponse.eta();

        if ((eta == null) || eta.isEmpty()) {
            return List.of();
        }

        return eta.stream()
                  .map(ArrivalMapper.INSTANCE::toDomain)
                  .toList();
    }
}
