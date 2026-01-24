package com.cta4j.bus.pattern.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

@NullMarked
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
        Objects.requireNonNull(type);
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);
    }
}
