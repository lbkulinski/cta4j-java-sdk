package com.cta4j.bus.pattern.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a point in a bus route pattern.
 *
 * @param sequence the position of this pattern point in the overall sequence of points
 * @param type the type of this pattern point
 * @param stopId the identifier of the stop associated with this pattern point, if applicable
 * @param stopName the name of the stop associated with this pattern point, if applicable
 * @param distanceToPatternPoint the distance to the next pattern point, if applicable
 * @param latitude the latitude coordinate of this pattern point
 * @param longitude the longitude coordinate of this pattern point
 */
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
    /**
     * Constructs a {@code PatternPoint}.
     *
     * @param sequence the position of the pattern point in the overall sequence of points
     * @param type the type of the pattern point
     * @param stopId the identifier of the stop associated with the pattern point, if applicable
     * @param stopName the name of the stop associated with the pattern point, if applicable
     * @param distanceToPatternPoint the distance to the next pattern point, if applicable
     * @param latitude the latitude coordinate of the pattern point
     * @param longitude the longitude coordinate of the pattern point
     * @throws NullPointerException if {@code type}, {@code latitude}, or {@code longitude} is {@code null}
     */
    public PatternPoint {
        Objects.requireNonNull(type);
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);
    }
}
