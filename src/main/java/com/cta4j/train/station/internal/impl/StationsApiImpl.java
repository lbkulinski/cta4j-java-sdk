package com.cta4j.train.station.internal.impl;

import com.cta4j.train.common.exception.Cta4jTrainException;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.station.StationsApi;
import com.cta4j.train.station.internal.mapper.StationMapper;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.Station;
import org.apache.hc.client5.http.fluent.Request;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class StationsApiImpl implements StationsApi {
    private static final TypeReference<List<CtaStation>> TYPE_REFERENCE = new TypeReference<>() {};

    private final TrainApiConfig config;

    public StationsApiImpl(TrainApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Station> list() {
        String stationsUrl = this.config.stationsUrl();

        URI stationsUri;

        try {
            stationsUri = URI.create(stationsUrl);
        } catch (IllegalArgumentException e) {
            throw new Cta4jTrainException("Invalid stations URL", stationsUrl, e);
        }

        String response;

        try {
            response = Request.get(stationsUri)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = Objects.requireNonNullElse(e.getMessage(), "Request failed");

            throw new Cta4jTrainException(message, stationsUrl, e);
        }

        List<CtaStation> stations;

        try {
            stations = JsonMapper.shared()
                                 .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jTrainException("Failed to parse response", stationsUrl, e);
        }

        return stations.stream()
                       .map(StationMapper.INSTANCE::toDomain)
                       .toList();
    }
}
