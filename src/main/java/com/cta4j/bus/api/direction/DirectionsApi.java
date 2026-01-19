package com.cta4j.bus.api.direction;

import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Provides access to direction-related endpoints of the CTA BusTime API.
 * <p>
 * This API allows retrieval of available travel directions for a given route.
 */
@NullMarked
public interface DirectionsApi {
    /**
     * Retrieves the available travel directions for the specified route (e.g., Northbound, Southbound).
     *
     * @param routeId the route identifier
     * @return a {@link List} of direction identifiers for the route;
     * an empty {@link List} if the route has no associated directions
     * @throws NullPointerException if {@code routeId} is {@code null}
     */
    List<String> findByRouteId(String routeId);
}
