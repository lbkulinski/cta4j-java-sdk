package com.cta4j.model.bus;

/**
 * A bus route and its direction affected by a detour.
 *
 * @param route the bus route identifier
 * @param direction the direction of the bus route (e.g., "Northbound", "Southbound")
 */
public record DetourRouteDirection(
    String route,

    String direction
) {
}
