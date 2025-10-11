package com.cta4j.model.train;

import com.cta4j.external.train.follow.CtaFollowPosition;

import java.math.BigDecimal;
import java.util.Objects;

public record TrainCoordinates(
    BigDecimal latitude,

    BigDecimal longitude,

    int heading
) {
    public static TrainCoordinates fromExternal(CtaFollowPosition position) {
        Objects.requireNonNull(position);

        return new TrainCoordinates(
            new BigDecimal(position.lat()),
            new BigDecimal(position.lon()),
            Integer.parseInt(position.heading())
        );
    }
}
