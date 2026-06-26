package com.cta4j.bus.route;

import com.cta4j.bus.route.model.Route;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Provides access to route-related endpoints of the CTA BusTime API.
 * <p>
 * This API allows retrieval of all available routes.
 */
@NullMarked
public interface RoutesApi {
    /**
     * Retrieves all available routes.
     *
     * @return a {@link List} of all available {@link Route}s, or an empty {@link List} if no routes are found
     * @throws Cta4jException if the API returns an error response or the response cannot be parsed
     */
    List<Route> list();
}
