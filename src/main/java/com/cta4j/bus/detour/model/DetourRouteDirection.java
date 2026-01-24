package com.cta4j.bus.detour.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Represents a route and direction affected by a detour.
 *
 * @param routeId The route ID of the detour
 * @param direction The direction of the detour
 */
@NullMarked
public record DetourRouteDirection(
    String routeId,

    String direction
) {
    /**
     * Creates a {@code DetourRouteDirection}.
     *
     * @param routeId the route ID of the detour
     * @param direction the direction of the detour
     * @throws NullPointerException if {@code routeId} or {@code direction} is {@code null}
     */
    public DetourRouteDirection {
        Objects.requireNonNull(routeId);
        Objects.requireNonNull(direction);
    }
}
