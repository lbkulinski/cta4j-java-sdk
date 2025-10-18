package com.cta4j.bus.model;

import java.math.BigDecimal;

/**
 * The coordinates and heading of a bus.
 *
 * @param latitude the latitude of the bus's current location
 * @param longitude the longitude of the bus's current location
 * @param heading the heading of the bus in degrees (0-359)
 */
public record BusCoordinates(
    BigDecimal latitude,

    BigDecimal longitude,

    Integer heading
) {
}
