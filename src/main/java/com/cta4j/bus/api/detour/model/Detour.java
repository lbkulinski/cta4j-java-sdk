package com.cta4j.bus.api.detour.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@NullMarked
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
        Objects.requireNonNull(id);
        Objects.requireNonNull(version);
        Objects.requireNonNull(description);
        Objects.requireNonNull(routeDirections);
        Objects.requireNonNull(startTime);
        Objects.requireNonNull(endTime);

        routeDirections.forEach(Objects::requireNonNull);
    }
}
