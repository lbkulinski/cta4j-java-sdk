package com.cta4j.train.station.internal.impl;

import com.cta4j.exception.Cta4jException;
import com.cta4j.internal.http.HttpClient;
import com.cta4j.train.internal.context.TrainApiContext;
import com.cta4j.train.station.StationsApi;
import com.cta4j.train.station.internal.mapper.StationMapper;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.Station;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class StationsApiImpl implements StationsApi {
    private final TrainApiContext context;

    public StationsApiImpl(TrainApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<Station> list() {
        String response = HttpClient.get(this.context.stationsUrl());

        TypeReference<List<CtaStation>> typeReference = new TypeReference<>() {};
        List<CtaStation> stations;

        try {
            stations = this.context.objectMapper()
                                   .readValue(response, typeReference);
        } catch (Exception e) {
            String message = String.format("Failed to parse response from %s", this.context.stationsUrl());

            throw new Cta4jException(message, e);
        }

        return stations.stream()
                       .map(StationMapper.INSTANCE::toDomain)
                       .toList();
    }
}
