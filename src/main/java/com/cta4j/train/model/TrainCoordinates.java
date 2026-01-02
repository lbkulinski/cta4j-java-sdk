package com.cta4j.train.model;

import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;

/**
 * The coordinates and heading of a train.
 *
 * @param latitude the latitude of the train's current location
 * @param longitude the longitude of the train's current location
 * @param heading the heading of the train in degrees (0-359)
 */
@NullMarked
@SuppressWarnings("ConstantConditions")
public record TrainCoordinates(
    BigDecimal latitude,

    BigDecimal longitude,

    int heading
) {
    public TrainCoordinates {
        if (latitude == null) {
            throw new IllegalArgumentException("latitude must not be null");
        }

        if (longitude == null) {
            throw new IllegalArgumentException("longitude must not be null");
        }
    }
}
