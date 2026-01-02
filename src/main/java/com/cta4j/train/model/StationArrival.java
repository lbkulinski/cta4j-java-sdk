package com.cta4j.train.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
@NullMarked
@SuppressWarnings("ConstantConditions")
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

    @Nullable
    String flags,

    @Nullable
    BigDecimal latitude,

    @Nullable
    BigDecimal longitude,

    @Nullable
    Integer heading
) {
    public StationArrival {
        if (stationId == null) {
            throw new IllegalArgumentException("stationId must not be null");
        }

        if (stopId == null) {
            throw new IllegalArgumentException("stopId must not be null");
        }

        if (stationName == null) {
            throw new IllegalArgumentException("stationName must not be null");
        }

        if (stopDescription == null) {
            throw new IllegalArgumentException("stopDescription must not be null");
        }

        if (run == null) {
            throw new IllegalArgumentException("run must not be null");
        }

        if (route == null) {
            throw new IllegalArgumentException("route must not be null");
        }

        if (destinationStopId == null) {
            throw new IllegalArgumentException("destinationStopId must not be null");
        }

        if (destinationName == null) {
            throw new IllegalArgumentException("destinationName must not be null");
        }

        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

        if (predictionTime == null) {
            throw new IllegalArgumentException("predictionTime must not be null");
        }

        if (arrivalTime == null) {
            throw new IllegalArgumentException("arrivalTime must not be null");
        }

        if (approaching == null) {
            throw new IllegalArgumentException("approaching must not be null");
        }

        if (scheduled == null) {
            throw new IllegalArgumentException("scheduled must not be null");
        }

        if (delayed == null) {
            throw new IllegalArgumentException("delayed must not be null");
        }

        if (faulted == null) {
            throw new IllegalArgumentException("faulted must not be null");
        }
    }

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
