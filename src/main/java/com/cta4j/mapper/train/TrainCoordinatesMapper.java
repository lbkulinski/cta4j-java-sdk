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
            new BigDecimal(position.lat()),
            new BigDecimal(position.lon()),
            Integer.parseInt(position.heading())
        );
    }
}
