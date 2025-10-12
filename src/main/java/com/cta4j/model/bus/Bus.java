package com.cta4j.model.bus;

import java.math.BigDecimal;

/**
 * A bus currently in service.
 *
 * @param id the unique identifier of the bus
 * @param latitude the latitude of the bus
 * @param longitude the longitude of the bus
 * @param heading the heading of the bus in degrees
 * @param route the route identifier the bus is serving
 * @param destination the destination of the bus
 * @param delayed whether the bus is currently delayed
 */
public record Bus(
    String id,

    BigDecimal latitude,

    BigDecimal longitude,

    Integer heading,

    String route,

    String destination,

    Boolean delayed
) {
}
