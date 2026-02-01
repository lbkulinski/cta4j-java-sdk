package com.cta4j.train.station.internal.impl;

import com.cta4j.train.internal.context.TrainApiContext;
import com.cta4j.train.station.StationsApi;
import com.cta4j.train.station.model.Station;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

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
        return List.of();
    }
}
