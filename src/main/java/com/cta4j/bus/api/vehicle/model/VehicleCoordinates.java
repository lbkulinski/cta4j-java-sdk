package com.cta4j.bus.api.vehicle.model;

import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.util.Objects;

@NullMarked
public record VehicleCoordinates(
    BigDecimal latitude,

    BigDecimal longitude,

    int heading
) {
    private static final int MIN_HEADING = 0;
    private static final int MAX_HEADING = 359;

    public VehicleCoordinates {
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);

        if ((heading < MIN_HEADING) || (heading > MAX_HEADING)) {
            String message = String.format("heading must be between %d and %d (inclusive)", MIN_HEADING, MAX_HEADING);

            throw new IllegalArgumentException(message);
        }
    }
}
