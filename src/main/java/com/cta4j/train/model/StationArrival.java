package com.cta4j.train.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

/**
 * An arrival prediction for a train at a station.
 *
 * @param stationId the unique identifier of the station
 * @param stopId the unique identifier of the stop
 * @param stationName the name of the station
 * @param stopDescription the description of the stop
 * @param run the unique identifier of the train run
 * @param route the route of the train
 * @param destinationStopId the unique identifier of the destination stop
 * @param destinationName the name of the destination
 * @param direction the direction of travel (0 = North, 1 = East, 2 = South, 3 = West)
 * @param predictionTime the time the prediction was made
 * @param arrivalTime the predicted arrival time
 * @param approaching whether the train is approaching the station
 * @param scheduled whether the prediction is based on a schedule
 * @param delayed whether the train is delayed
 * @param faulted whether there is a fault affecting the train
 * @param flags additional flags associated with the prediction
 * @param latitude the latitude of the train's current location
 * @param longitude the longitude of the train's current location
 * @param heading the heading of the train in degrees (0-359)
 */
public record StationArrival(
    String stationId,

    String stopId,

    String stationName,

    String stopDescription,

    String run,

    Route route,

    String destinationStopId,

    String destinationName,

    Integer direction,

    Instant predictionTime,

    Instant arrivalTime,

    Boolean approaching,

    Boolean scheduled,

    Boolean delayed,

    Boolean faulted,

    String flags,

    BigDecimal latitude,

    BigDecimal longitude,

    Integer heading
) {
    /**
     * Calculates the estimated time of arrival (ETA) in minutes from the prediction time to the arrival time. If the
     * arrival time is before the prediction time, it returns 0.
     *
     * @return the ETA in minutes, or 0 if the arrival time is before the prediction time.
     */
    public long etaMinutes() {
        long minutes = Duration.between(this.predictionTime, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
