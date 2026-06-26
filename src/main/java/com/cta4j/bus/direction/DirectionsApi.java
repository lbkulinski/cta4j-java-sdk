package com.cta4j.bus.direction;

import com.cta4j.exception.Cta4jException;
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
     * @return a {@link List} of direction identifiers for the route, or an empty {@link List} if no directions are
     * found for the route
     * @throws NullPointerException if {@code routeId} is {@code null}
     * @throws Cta4jException if the API returns an error response or the response cannot be parsed
     */
    List<String> findByRouteId(String routeId);
}
