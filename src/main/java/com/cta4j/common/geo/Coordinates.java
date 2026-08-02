package com.cta4j.common.geo;

import com.cta4j.common.internal.geo.GeoConstants;
import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.util.Objects;

/// Represents geographic coordinates.
///
/// @param latitude the latitude of these coordinates, in degrees (-90-90)
/// @param longitude the longitude of these coordinates, in degrees (-180-180)
/// @param heading the heading of these coordinates in degrees (0-359)
@NullMarked
public record Coordinates(
    BigDecimal latitude,
    BigDecimal longitude,
    int heading
) {
    /// Constructs a `Coordinates`.
    ///
    /// @param latitude the latitude of the coordinates, in degrees (-90-90)
    /// @param longitude the longitude of the coordinates, in degrees (-180-180)
    /// @param heading the heading of the coordinates in degrees (0-359)
    /// @throws NullPointerException if `latitude` or `longitude` is `null`
    /// @throws IllegalArgumentException if `latitude` is not between -90 and 90 (inclusive), `longitude` is not
    /// between -180 and 180 (inclusive), or `heading` is not between 0 and 359 (inclusive)
    public Coordinates {
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);

        if ((latitude.compareTo(GeoConstants.MIN_LATITUDE) < 0) ||
            (latitude.compareTo(GeoConstants.MAX_LATITUDE) > 0)) {
            String message = "latitude must be between %s and %s (inclusive)".formatted(
                GeoConstants.MIN_LATITUDE,
                GeoConstants.MAX_LATITUDE
            );

            throw new IllegalArgumentException(message);
        }

        if ((longitude.compareTo(GeoConstants.MIN_LONGITUDE) < 0) ||
            (longitude.compareTo(GeoConstants.MAX_LONGITUDE) > 0)) {
            String message = "longitude must be between %s and %s (inclusive)".formatted(
                GeoConstants.MIN_LONGITUDE,
                GeoConstants.MAX_LONGITUDE
            );

            throw new IllegalArgumentException(message);
        }

        if ((heading < GeoConstants.MIN_HEADING) || (heading > GeoConstants.MAX_HEADING)) {
            String message = "heading must be between %d and %d (inclusive)".formatted(
                GeoConstants.MIN_HEADING,
                GeoConstants.MAX_HEADING
            );

            throw new IllegalArgumentException(message);
        }
    }
}
