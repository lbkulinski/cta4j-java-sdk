package com.cta4j.model.train;

import java.math.BigDecimal;

/**
 * The coordinates and heading of a train.
 *
 * @param latitude the latitude of the train's current location.
 * @param longitude the longitude of the train's current location.
 * @param heading the heading of the train in degrees (0-359).
 */
public record TrainCoordinates(
    BigDecimal latitude,

    BigDecimal longitude,

    int heading
) {
}
