package com.cta4j.bus.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * A bus stop.
 *
 * @param id the unique identifier of the bus stop
 * @param name the name of the bus stop
 * @param latitude the latitude coordinate of the bus stop
 * @param longitude the longitude coordinate of the bus stop
 * @param detoursAdded a list of detour identifiers that have been added to this stop
 * @param detoursRemoved a list of detour identifiers that have been removed from this stop
 * @param gtfsSequence the GTFS sequence number of the bus stop
 * @param adaAccessible indicates whether the bus stop is ADA accessible
 */
public record Stop(
    String id,

    String name,

    BigDecimal latitude,

    BigDecimal longitude,

    List<Integer> detoursAdded,

    List<Integer> detoursRemoved,

    Integer gtfsSequence,

    Boolean adaAccessible
) {
}
