package com.cta4j.train.station.internal.impl;

import com.cta4j.exception.Cta4jException;
import com.cta4j.common.internal.http.HttpClient;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.station.StationsApi;
import com.cta4j.train.station.internal.mapper.StationMapper;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.Station;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

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
        String response = HttpClient.get(this.config.stationsUrl());

        List<CtaStation> stations;

        try {
            stations = JsonMapper.shared()
                                 .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(this.config.stationsUrl());

            throw new Cta4jException(message, e);
        }

        return stations.stream()
                       .map(StationMapper.INSTANCE::toDomain)
                       .toList();
    }
}
