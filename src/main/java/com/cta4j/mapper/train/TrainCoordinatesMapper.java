package com.cta4j.mapper.train;

import com.cta4j.external.train.follow.CtaFollowPosition;
import com.cta4j.model.train.TrainCoordinates;

import java.math.BigDecimal;
import java.util.Objects;

public final class TrainCoordinatesMapper {
    private TrainCoordinatesMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static TrainCoordinates fromExternal(CtaFollowPosition position) {
        Objects.requireNonNull(position);

        return new TrainCoordinates(
            (position.lat() == null) ? null : new BigDecimal(position.lat()),
            (position.lon() == null) ? null : new BigDecimal(position.lon()),
            (position.heading() == null) ? null : Integer.parseInt(position.heading())
        );
    }
}
