package com.cta4j.bus.detour;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.detour.model.Detour;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Provides access to detour-related endpoints of the CTA BusTime API.
 * <p>
 * This API allows retrieval of active service detours across all routes,
 * or filtered by route and direction.
 */
@NullMarked
public interface DetoursApi {
    /**
     * Retrieves all active detours.
     *
     * @return a {@link List} of active {@link Detour}s, or an empty {@link List} if no detours are found
     * @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
     */
    List<Detour> list();

    /**
     * Retrieves all active detours for the specified route ID.
     *
     * @param routeId the route ID
     * @return a {@link List} of {@link Detour}s associated with the route ID, or an empty {@link List} if no detours
     * are found for the route ID
     * @throws NullPointerException if {@code routeId} is {@code null}
     * @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
     */
    List<Detour> findByRouteId(String routeId);

    /**
     * Retrieves all active detours for the specified route ID and direction.
     *
     * @param routeId the route ID
     * @param direction the travel direction (e.g. Northbound, Southbound)
     * @return a {@link List} of {@link Detour}s associated with the route ID and direction, or an empty {@link List}
     * if no detours are found for the route ID and direction
     * @throws NullPointerException if {@code routeId} or {@code direction} is {@code null}
     * @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
     */
    List<Detour> findByRouteIdAndDirection(String routeId, String direction);
}
