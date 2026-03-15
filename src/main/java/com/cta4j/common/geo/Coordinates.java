package com.cta4j.common.geo;

import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents geographic coordinates.
 *
 * @param latitude the latitude of these coordinates
 * @param longitude the longitude of these coordinates
 * @param heading the heading of these coordinates in degrees (0-359)
 */
@NullMarked
public record Coordinates(
    BigDecimal latitude,

    BigDecimal longitude,

    int heading
) {
    /**
     * Constructs a {@code Coordinates}.
     *
     * @param latitude the latitude of the coordinates
     * @param longitude the longitude of the coordinates
     * @param heading the heading of the coordinates in degrees (0-359)
     * @throws NullPointerException if {@code latitude} or {@code longitude} is {@code null}
     * @throws IllegalArgumentException if {@code latitude} is not between -90 and 90 (inclusive), {@code longitude}
     * is not between -180 and 180 (inclusive), or {@code heading} is not between 0 and 359 (inclusive)
     */
    public Coordinates {
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);

        if ((latitude.compareTo(GeoConstants.MIN_LATITUDE) < 0) ||
            (latitude.compareTo(GeoConstants.MAX_LATITUDE) > 0)) {
            String message = String.format(
                "latitude must be between %s and %s (inclusive)",
                GeoConstants.MIN_LATITUDE,
                GeoConstants.MAX_LATITUDE
            );

            throw new IllegalArgumentException(message);
        }

        if ((longitude.compareTo(GeoConstants.MIN_LONGITUDE) < 0) ||
            (longitude.compareTo(GeoConstants.MAX_LONGITUDE) > 0)) {
            String message = String.format(
                "longitude must be between %s and %s (inclusive)",
                GeoConstants.MIN_LONGITUDE,
                GeoConstants.MAX_LONGITUDE
            );

            throw new IllegalArgumentException(message);
        }

        if ((heading < GeoConstants.MIN_HEADING) || (heading > GeoConstants.MAX_HEADING)) {
            String message = String.format(
                "heading must be between %d and %d (inclusive)",
                GeoConstants.MIN_HEADING,
                GeoConstants.MAX_HEADING
            );

            throw new IllegalArgumentException(message);
        }
    }
}
