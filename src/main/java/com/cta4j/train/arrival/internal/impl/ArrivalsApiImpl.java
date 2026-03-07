package com.cta4j.train.arrival.internal.impl;

import com.cta4j.train.arrival.ArrivalsApi;
import com.cta4j.train.arrival.model.Arrival;
import com.cta4j.train.arrival.query.MapArrivalQuery;
import com.cta4j.train.arrival.query.StopArrivalQuery;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@ApiStatus.Internal
public final class ArrivalsApiImpl implements ArrivalsApi {
    @Override
    public List<Arrival> findByMapId(MapArrivalQuery query) {
        return List.of();
    }

    @Override
    public List<Arrival> findByStopId(StopArrivalQuery query) {
        return List.of();
    }
}
