package com.cta4j.bus.pattern.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
public record RoutePattern(
    String patternId,

    int patternCount,

    String direction,

    List<PatternPoint> points,

    @Nullable
    String detourId,

    @Nullable
    List<PatternPoint> detourPoints
) {
    public RoutePattern {
        Objects.requireNonNull(patternId);
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
