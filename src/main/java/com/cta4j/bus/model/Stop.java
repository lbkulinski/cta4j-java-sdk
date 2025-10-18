package com.cta4j.bus.model;

import java.math.BigDecimal;

/**
 * A bus stop.
 *
 * @param id the unique identifier of the bus stop
 * @param name the name of the bus stop
 * @param latitude the latitude coordinate of the bus stop
 * @param longitude the longitude coordinate of the bus stop
 */
public record Stop(
    String id,

    String name,

    BigDecimal latitude,

    BigDecimal longitude
) {
}
