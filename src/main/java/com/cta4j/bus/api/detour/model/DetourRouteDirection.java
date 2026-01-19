package com.cta4j.bus.api.detour.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public record DetourRouteDirection(
    String route,

    String direction
) {
    public DetourRouteDirection {
        Objects.requireNonNull(route);
        Objects.requireNonNull(direction);
    }
}
