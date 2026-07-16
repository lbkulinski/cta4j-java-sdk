package com.cta4j.train.arrival.internal.impl;

import com.cta4j.train.arrival.ArrivalsApi;
import com.cta4j.train.arrival.exception.ArrivalsErrorCode;
import com.cta4j.train.arrival.exception.Cta4jArrivalsException;
import com.cta4j.train.arrival.internal.wire.CtaArrivalsResponse;
import com.cta4j.train.arrival.query.MapArrivalQuery;
import com.cta4j.train.arrival.query.StopArrivalQuery;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.internal.mapper.ArrivalMapper;
import com.cta4j.train.common.internal.util.TrainApiConstants;
import com.cta4j.train.common.internal.wire.CtaArrival;
import com.cta4j.train.common.internal.wire.CtaResponse;
import com.cta4j.train.common.model.Arrival;
import com.cta4j.common.train.TrainLine;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ArrivalsApiImpl implements ArrivalsApi {
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
            .setPath(TrainApiConstants.ARRIVALS_ENDPOINT)
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
            .setPath(TrainApiConstants.ARRIVALS_ENDPOINT)
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

        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = Objects.requireNonNullElse(e.getMessage(), "Request failed");

            throw new Cta4jArrivalsException(message, e);
        }

        CtaResponse<CtaArrivalsResponse> ctaResponse;

        try {
            ctaResponse = JsonMapper.shared()
                                    .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jArrivalsException("Failed to parse response", e);
        }

        CtaArrivalsResponse arrivalsResponse = ctaResponse.ctatt();

        int errCd;

        try {
            errCd = Integer.parseInt(arrivalsResponse.errCd());
        } catch (NumberFormatException e) {
            throw new Cta4jArrivalsException("Failed to parse error code", e);
        }

        if (errCd < 0) {
            throw new Cta4jArrivalsException("Unknown error code", errCd);
        }

        ArrivalsErrorCode errorCode = ArrivalsErrorCode.fromCode(errCd);

        if (errorCode == ArrivalsErrorCode.INVALID_MAPID || errorCode == ArrivalsErrorCode.INVALID_STPID) {
            return List.of();
        }

        if (errorCode != ArrivalsErrorCode.OK) {
            String errNm = arrivalsResponse.errNm();

            String message = errNm == null || errNm.isBlank()
                ? "An unknown error occurred."
                : errNm;

            throw new Cta4jArrivalsException(message, errCd);
        }

        List<CtaArrival> eta = arrivalsResponse.eta();

        if (eta == null || eta.isEmpty()) {
            return List.of();
        }

        return eta.stream()
                  .map(ArrivalMapper.INSTANCE::toDomain)
                  .toList();
    }
}
