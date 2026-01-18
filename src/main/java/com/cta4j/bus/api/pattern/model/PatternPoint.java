package com.cta4j.bus.api.pattern.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

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
    public PatternPoint(
        int sequence,
        @Nullable PatternPointType type,
        @Nullable String stopId,
        @Nullable String stopName,
        @Nullable BigDecimal distanceToPatternPoint,
        @Nullable BigDecimal latitude,
        @Nullable BigDecimal longitude
    ) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }

        if (latitude == null) {
            throw new IllegalArgumentException("latitude must not be null");
        }

        if (longitude == null) {
            throw new IllegalArgumentException("longitude must not be null");
        }

        this.sequence = sequence;
        this.type = type;
        this.stopId = stopId;
        this.stopName = stopName;
        this.distanceToPatternPoint = distanceToPatternPoint;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
