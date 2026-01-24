package com.cta4j.bus.detour;

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
     * @return a {@link List} of active {@link Detour}s;
     * an empty {@link List} if no detours are currently active
     */
    List<Detour> list();

    /**
     * Retrieves all active detours for the specified route ID.
     *
     * @param routeId the route ID
     * @return a {@link List} of {@link Detour}s associated with the route ID;
     * an empty {@link List} if no detours exist for the route ID
     * @throws NullPointerException if {@code routeId} is {@code null}
     */
    List<Detour> findByRouteId(String routeId);

    /**
     * Retrieves all active detours for the specified route ID and direction.
     *
     * @param routeId the route ID
     * @param direction the travel direction (e.g. Northbound, Southbound)
     * @return a {@link List} of {@link Detour}s associated with the route ID and direction;
     * an empty {@link List} if no detours exist for the route ID and direction
     * @throws NullPointerException if {@code routeId} or {@code direction} is {@code null}
     */
    List<Detour> findByRouteIdAndDirection(String routeId, String direction);
}
