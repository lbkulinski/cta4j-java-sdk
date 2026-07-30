package com.cta4j.bus.direction;

import com.cta4j.bus.common.exception.Cta4jBusException;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/// Provides access to direction-related endpoints of the CTA Bus Tracker API.
///
/// This API allows retrieval of available travel directions for a given route.
@NullMarked
public interface DirectionsApi {
    /// Retrieves the available travel directions for the specified route.
    ///
    /// @param routeId the route ID
    /// @return a [List] of direction identifiers for the route (e.g., "Northbound", "Southbound"), or an empty [List]
    /// if no directions are found for the route
    /// @throws NullPointerException if `routeId` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    List<String> findByRouteId(String routeId);
}
