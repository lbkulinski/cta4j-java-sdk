package com.cta4j.bus.stop.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/// Represents a bus stop.
///
/// @apiNote `gtfsSequence` is not well-documented by the CTA. As such, its presence here is primarily for completeness
/// and may not be populated or described correctly.
///
/// @param id the unique identifier of this stop
/// @param name the display name of this stop (e.g., "Clark & Addison")
/// @param latitude the latitude coordinate of this stop
/// @param longitude the longitude coordinate of this stop
/// @param detoursAdded the [List] of detour IDs which temporarily add service to this stop, if applicable
/// @param detoursRemoved the [List] of detour IDs which temporarily remove service from this stop, if applicable
/// @param gtfsSequence the GTFS sequence number of this stop, if applicable
/// @param adaAccessible whether this stop is ADA accessible, if known
@NullMarked
public record Stop(
    String id,
    String name,
    BigDecimal latitude,
    BigDecimal longitude,
    @Nullable List<Integer> detoursAdded,
    @Nullable List<Integer> detoursRemoved,
    @Nullable Integer gtfsSequence,
    @Nullable Boolean adaAccessible
) {
    /// Constructs a `Stop`.
    ///
    /// @param id the unique identifier of the stop
    /// @param name the display name of the stop (e.g., "Clark & Addison")
    /// @param latitude the latitude coordinate of the stop
    /// @param longitude the longitude coordinate of the stop
    /// @param detoursAdded the [List] of detour IDs which temporarily add service to the stop, if applicable
    /// @param detoursRemoved the [List] of detour IDs which temporarily remove service from the stop, if applicable
    /// @param gtfsSequence the GTFS sequence number of the stop, if applicable
    /// @param adaAccessible whether the stop is ADA accessible, if known
    /// @throws NullPointerException if `id`, `name`, `latitude`, or `longitude` is `null`, or if any element of
    /// `detoursAdded` or `detoursRemoved` is `null`
    public Stop {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);

        if (detoursAdded != null) {
            detoursAdded = List.copyOf(detoursAdded);
        }

        if (detoursRemoved != null) {
            detoursRemoved = List.copyOf(detoursRemoved);
        }
    }
}
