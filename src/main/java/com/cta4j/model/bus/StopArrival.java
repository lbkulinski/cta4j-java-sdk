package com.cta4j.model.bus;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

/**
 * An arrival prediction for a bus at a specific stop.
 *
 * @param predictionType the type of prediction (arrival or departure)
 * @param stopName the name of the bus stop
 * @param stopId the unique identifier of the bus stop
 * @param vehicleId the unique identifier of the bus vehicle
 * @param distanceToStop the distance from the bus to the stop in feet
 * @param route the bus route identifier
 * @param routeDesignator additional designator for the route, if any
 * @param routeDirection the direction of the bus route (e.g., Northbound, Southbound)
 * @param destination the final destination of the bus
 * @param arrivalTime the predicted arrival time at the stop
 * @param delayed indicates whether the bus is delayed
 */
public record StopArrival(
    BusPredictionType predictionType,

    String stopName,

    String stopId,

    String vehicleId,

    BigInteger distanceToStop,

    String route,

    String routeDesignator,

    String routeDirection,

    String destination,

    Instant arrivalTime,

    Boolean delayed
) {
    /**
     * Calculates the estimated time of arrival (ETA) in minutes from the current time to the predicted arrival time.
     * If the arrival time is in the past, it returns 0.
     *
     * @return the ETA in minutes, or 0 if the arrival time is in the past
     */
    public Long etaMinutes() {
        Instant now = Instant.now();

        long minutes = Duration.between(now, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
