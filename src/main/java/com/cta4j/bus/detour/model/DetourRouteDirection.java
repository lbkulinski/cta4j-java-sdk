package com.cta4j.bus.detour.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Represents a route and direction affected by a detour.
 *
 * @param route The route ID of the detour
 * @param direction The direction of the detour
 */
@NullMarked
public record DetourRouteDirection(
    String route,

    String direction
) {
    /**
     * Creates a {@code DetourRouteDirection}.
     *
     * @param route the route ID of the detour
     * @param direction the direction of the detour
     * @throws NullPointerException if {@code route} or {@code direction} is {@code null}
     */
    public DetourRouteDirection {
        Objects.requireNonNull(route);
        Objects.requireNonNull(direction);
    }
}
