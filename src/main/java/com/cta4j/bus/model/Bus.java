package com.cta4j.bus.model;

import java.util.List;

/**
 * A bus currently in service.
 *
 * @param id the unique identifier of the bus
 * @param coordinates the coordinates and heading of the bus
 * @param arrivals the list of upcoming bus arrivals for the bus
 * @param route the route identifier the bus is serving
 * @param destination the destination of the bus
 * @param delayed whether the bus is currently delayed
 */
public record Bus(
    String id,

    String route,

    String destination,

    BusCoordinates coordinates,

    List<UpcomingBusArrival> arrivals,

    Boolean delayed
) {
}
