package com.cta4j.bus.detour;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.detour.model.Detour;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/// Provides access to detour-related endpoints of the CTA Bus Tracker API.
///
/// This API allows retrieval of active service detours across all routes, or filtered by route and direction.
@NullMarked
public interface DetoursApi {
    /// Retrieves all active detours.
    ///
    /// @return a [List] of active [Detour]s, or an empty [List] if no detours are found
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    List<Detour> list();

    /// Retrieves all active detours for the specified route ID.
    ///
    /// @param routeId the route ID
    /// @return a [List] of [Detour]s associated with the route ID, or an empty [List] if no detours are found for the
    /// route ID
    /// @throws NullPointerException if `routeId` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    List<Detour> findByRouteId(String routeId);

    /// Retrieves all active detours for the specified route ID and direction.
    ///
    /// @param routeId the route ID
    /// @param direction the direction (e.g., "Northbound", "Southbound")
    /// @return a [List] of [Detour]s associated with the route ID and direction, or an empty [List] if no detours are
    /// found for the route ID and direction
    /// @throws NullPointerException if `routeId` or `direction` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    List<Detour> findByRouteIdAndDirection(String routeId, String direction);
}
