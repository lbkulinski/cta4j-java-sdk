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
import com.cta4j.train.common.internal.context.TrainApiContext;
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

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class ArrivalsApiImpl implements ArrivalsApi {
    private static final String ARRIVALS_ENDPOINT = String.format("%s/ttarrivals.aspx", ApiUtils.API_PREFIX);

    private final TrainApiContext context;

    public ArrivalsApiImpl(TrainApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<Arrival> findByMapId(MapArrivalQuery query) {
        Objects.requireNonNull(query);

        URIBuilder builder = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(ARRIVALS_ENDPOINT)
            .addParameter("mapid", query.mapId())
            .addParameter("key", this.context.apiKey())
            .addParameter("outputType", "JSON");

        return this.makeRequest(builder, query.line(), query.maxResults());
    }

    @Override
    public List<Arrival> findByStopId(StopArrivalQuery query) {
        Objects.requireNonNull(query);

        URIBuilder builder = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(ARRIVALS_ENDPOINT)
            .addParameter("stpid", query.stopId())
            .addParameter("key", this.context.apiKey())
            .addParameter("outputType", "JSON");

        return this.makeRequest(builder, query.line(), query.maxResults());
    }

    private List<Arrival> makeRequest(URIBuilder builder, @Nullable TrainLine line, @Nullable Integer maxResults) {
        if (line != null) {
            builder.addParameter("rt", line.getCode());
        }

        if (maxResults != null) {
            builder.addParameter("max", maxResults.toString());
        }

        String url = builder.toString();

        String response = HttpClient.get(url);

        TypeReference<CtaResponse<CtaArrivalsResponse>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaArrivalsResponse> ctaResponse;

        try {
            ctaResponse = this.context.objectMapper()
                                      .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", ARRIVALS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaArrivalsResponse arrivalsResponse = ctaResponse.ctatt();

        if (arrivalsResponse.errCd() != 0) {
            String errorMessage = Objects.requireNonNullElse(arrivalsResponse.errNm(), "Unknown error");

            CtaError error = new CtaError(arrivalsResponse.errCd(), errorMessage);

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
