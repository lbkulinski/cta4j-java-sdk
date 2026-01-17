package com.cta4j.bus.api.vehicle.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

/**
 * The coordinates and heading of a bus.
 *
 * @param latitude the latitude of the bus's current location
 * @param longitude the longitude of the bus's current location
 * @param heading the heading of the bus in degrees (0-359)
 */
@NullMarked
public record VehicleCoordinates(
    BigDecimal latitude,

    BigDecimal longitude,

    int heading
) {
    public VehicleCoordinates(
        @Nullable BigDecimal latitude,
        @Nullable BigDecimal longitude,
        int heading
    ) {
        if (latitude == null) {
            throw new IllegalArgumentException("latitude must not be null");
        }

        if (longitude == null) {
            throw new IllegalArgumentException("longitude must not be null");
        }

        if ((heading < 0) || (heading > 359)) {
            throw new IllegalArgumentException("heading must be between 0 and 359 (inclusive)");
        }

        this.latitude = latitude;
        this.longitude = longitude;
        this.heading = heading;
    }
}
