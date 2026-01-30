package com.cta4j.bus.vehicle.model;

import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents the coordinates of a vehicle.
 *
 * @param latitude the latitude of these coordinates
 * @param longitude the longitude of these coordinates
 * @param heading the heading of these coordinates in degrees (0-359)
 */
@NullMarked
public record VehicleCoordinates(
    BigDecimal latitude,

    BigDecimal longitude,

    int heading
) {
    /**
     * The minimum valid heading.
     */
    private static final int MIN_HEADING = 0;

    /**
     * The maximum valid heading.
     */
    private static final int MAX_HEADING = 359;

    /**
     * Constructs a {@code VehicleCoordinates}.
     *
     * @param latitude the latitude of the coordinates
     * @param longitude the longitude of the coordinates
     * @param heading the heading of the coordinates in degrees (0-359)
     * @throws NullPointerException if {@code latitude} or {@code longitude} is {@code null}
     * @throws IllegalArgumentException if {@code heading} is not between 0 and 359 (inclusive)
     */
    public VehicleCoordinates {
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);

        if ((heading < MIN_HEADING) || (heading > MAX_HEADING)) {
            String message = String.format("heading must be between %d and %d (inclusive)", MIN_HEADING, MAX_HEADING);

            throw new IllegalArgumentException(message);
        }
    }
}
