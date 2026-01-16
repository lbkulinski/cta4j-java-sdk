package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

@NullMarked
@SuppressWarnings("ConstantConditions")
public record PatternPoint(
    int sequence,

    PatternPointType type,

    @Nullable
    String stopId,

    @Nullable
    String stopName,

    @Nullable
    BigDecimal distanceToPatternPoint,

    BigDecimal latitude,

    BigDecimal longitude
) {
    public PatternPoint {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }

        if (latitude == null) {
            throw new IllegalArgumentException("latitude must not be null");
        }

        if (longitude == null) {
            throw new IllegalArgumentException("longitude must not be null");
        }
    }
}
