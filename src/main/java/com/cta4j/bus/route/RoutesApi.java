package com.cta4j.bus.route;

import com.cta4j.bus.route.model.Route;
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
     * @return a {@link List} of all available {@link Route}s
     */
    List<Route> list();
}
