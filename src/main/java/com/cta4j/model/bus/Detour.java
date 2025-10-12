package com.cta4j.model.bus;

import java.time.Instant;
import java.util.List;

/**
 * A detour affecting one or more bus routes.
 *
 * @param id the unique identifier of the detour
 * @param version the version number of the detour
 * @param active whether the detour is currently active
 * @param description a description of the detour
 * @param routeDirections the list of route directions affected by the detour
 * @param startTime the start time of the detour
 * @param endTime the end time of the detour
 */
public record Detour(
    String id,

    int version,

    boolean active,

    String description,

    List<DetourRouteDirection> routeDirections,

    Instant startTime,

    Instant endTime
) {
}
