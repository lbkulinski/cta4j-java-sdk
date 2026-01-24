package com.cta4j.bus.detour.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Represents a service detour affecting one or more routes and directions within a specific time window.
 *
 * @param id The unique ID of the detour
 * @param version The version of the detour
 * @param active Whether the detour is currently active
 * @param description The human-readable description of the detour
 * @param routeDirections The routes and directions affected by the detour
 * @param startTime The time at which the detour begins
 * @param endTime The time at which the detour ends
 * @param dataFeed The identifier for the data feed that supplied the detour, or {@code null} if not available
 */
@NullMarked
public record Detour(
    String id,

    String version,

    boolean active,

    String description,

    List<DetourRouteDirection> routeDirections,

    Instant startTime,

    Instant endTime,

    @Nullable
    String dataFeed
) {
    /**
     * Creates a {@code Detour}.
     *
     * @param id the detour ID
     * @param version the detour version
     * @param active whether the detour is active
     * @param description the detour description
     * @param routeDirections the {@code List} of route directions affected by the detour
     * @param startTime the detour start time
     * @param endTime the detour end time
     * @param dataFeed the data feed ID, or {@code null} if not available
     * @throws NullPointerException if {@code id}, {@code version}, {@code description}, {@code routeDirections},
     * {@code startTime}, or {@code endTime} is {@code null}
     */
    public Detour {
        Objects.requireNonNull(id);
        Objects.requireNonNull(version);
        Objects.requireNonNull(description);
        Objects.requireNonNull(routeDirections);
        Objects.requireNonNull(startTime);
        Objects.requireNonNull(endTime);

        routeDirections.forEach(Objects::requireNonNull);

        routeDirections = List.copyOf(routeDirections);
    }
}
