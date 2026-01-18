package com.cta4j.bus.api.pattern.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

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
    public RoutePattern(
        @Nullable String patternId,
        int patternCount,
        @Nullable String direction,
        @Nullable List<@Nullable PatternPoint> points,
        @Nullable String detourId,
        @Nullable List<@Nullable PatternPoint> detourPoints
    ) {
        if (patternId == null) {
            throw new IllegalArgumentException("patternId must not be null");
        }

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

        List<PatternPoint> detourPointsCopy = null;

        if (detourPoints != null) {
            for (PatternPoint detourPoint : detourPoints) {
                if (detourPoint == null) {
                    throw new IllegalArgumentException("detourPoints must not contain null elements");
                }
            }

            detourPointsCopy = List.copyOf(detourPoints);
        }

        this.patternId = patternId;
        this.patternCount = patternCount;
        this.direction = direction;
        this.points = List.copyOf(points);
        this.detourId = detourId;
        this.detourPoints = detourPointsCopy;
    }
}
