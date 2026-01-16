package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
@SuppressWarnings("ConstantConditions")
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
        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

        if (points == null) {
            throw new IllegalArgumentException("points must not be null");
        }

        for (PatternPoint point : points) {
            if (point == null) {
                throw new IllegalArgumentException("points must not contain null elements");
            }
        }
    }
}
