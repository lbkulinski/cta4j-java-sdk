package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@NullMarked
@SuppressWarnings("ConstantConditions")
public record Detour(
    String id,

    String version,

    boolean active,

    String description,

    List<DetourRouteDirection> routeDirections,

    Instant startTime,

    Instant endTime,

    @Nullable
    String dataFeed
) {
    public Detour {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }

        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }

        if (routeDirections == null) {
            throw new IllegalArgumentException("routeDirections must not be null");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("startTime must not be null");
        }

        if (endTime == null) {
            throw new IllegalArgumentException("endTime must not be null");
        }

        for (DetourRouteDirection routeDirection : routeDirections) {
            if (routeDirection == null) {
                throw new IllegalArgumentException("routeDirections must not contain null elements");
            }
        }
    }
}
