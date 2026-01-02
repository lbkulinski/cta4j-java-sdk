package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;

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
@NullMarked
@SuppressWarnings("ConstantConditions")
public record Detour(
    String id,

    String version,

    Boolean active,

    String description,

    List<DetourRouteDirection> routeDirections,

    Instant startTime,

    Instant endTime
) {
    public Detour {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }

        if (active == null) {
            throw new IllegalArgumentException("active must not be null");
        }

        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }

        if (routeDirections == null) {
            throw new IllegalArgumentException("routeDirections must not be null");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("startTime must not be null");
        }

        if (endTime == null) {
            throw new IllegalArgumentException("endTime must not be null");
        }

        for (DetourRouteDirection routeDirection : routeDirections) {
            if (routeDirection == null) {
                throw new IllegalArgumentException("routeDirections must not contain null elements");
            }
        }
    }
}
