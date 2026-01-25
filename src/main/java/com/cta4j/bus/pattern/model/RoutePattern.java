package com.cta4j.bus.pattern.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Represents a bus route pattern.
 *
 * @param id the unique identifier of the route pattern
 * @param length the length of the route pattern in feet
 * @param direction the direction of the route pattern (e.g., "Northbound", "Southbound")
 * @param points the list of pattern points that make up the route pattern
 * @param detourId the identifier of the detour, if applicable
 * @param detourPoints the list of pattern points for the detour, if applicable
 */
@NullMarked
public record RoutePattern(
    String id,

    int length,

    String direction,

    List<PatternPoint> points,

    @Nullable
    String detourId,

    @Nullable
    List<PatternPoint> detourPoints
) {
    /**
     * Constructs a {@code RoutePattern}.
     *
     * @param id the unique identifier of the route pattern
     * @param length the length of the route pattern in feet
     * @param direction the direction of the route pattern (e.g., "Northbound", "Southbound")
     * @param points the list of pattern points that make up the route pattern
     * @param detourId the identifier of the detour, if applicable
     * @param detourPoints the list of pattern points for the detour, if applicable
     */
    public RoutePattern {
        Objects.requireNonNull(id);
        Objects.requireNonNull(direction);
        Objects.requireNonNull(points);

        points.forEach(Objects::requireNonNull);

        points = List.copyOf(points);

        if (detourPoints != null) {
            detourPoints.forEach(Objects::requireNonNull);

            detourPoints = List.copyOf(detourPoints);
        }
    }
}
