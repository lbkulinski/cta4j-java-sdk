package com.cta4j.bus.detour.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/// Represents a service detour affecting one or more routes and directions within a specific time window.
///
/// @apiNote `dataFeed` is not well-documented by the CTA. As such, its presence here is primarily for completeness and
/// may not be populated or described correctly.
///
/// @param id the unique identifier of this detour
/// @param version the version of this detour
/// @param active whether this detour is currently active
/// @param description the human-readable description of this detour
/// @param routeDirections the routes and directions affected by this detour
/// @param startTime the time at which this detour begins
/// @param endTime the time at which this detour ends
/// @param dataFeed the identifier for the data feed that supplied this detour, if applicable
@NullMarked
public record Detour(
    String id,
    String version,
    boolean active,
    String description,
    List<DetourRouteDirection> routeDirections,
    Instant startTime,
    Instant endTime,
    @Nullable String dataFeed
) {
    /// Constructs a `Detour`.
    ///
    /// @param id the unique identifier of the detour
    /// @param version the version of the detour
    /// @param active whether the detour is currently active
    /// @param description the human-readable description of the detour
    /// @param routeDirections the routes and directions affected by the detour
    /// @param startTime the time at which the detour begins
    /// @param endTime the time at which the detour ends
    /// @param dataFeed the identifier for the data feed that supplied the detour, if applicable
    /// @throws NullPointerException if `id`, `version`, `description`, `routeDirections`, `startTime`, or `endTime` is
    /// `null`, or if any element of `routeDirections` is `null`
    public Detour {
        Objects.requireNonNull(id);
        Objects.requireNonNull(version);
        Objects.requireNonNull(description);
        Objects.requireNonNull(routeDirections);
        Objects.requireNonNull(startTime);
        Objects.requireNonNull(endTime);

        routeDirections = List.copyOf(routeDirections);
    }
}
