package com.cta4j.train.location.model;

import com.cta4j.common.geo.Coordinates;
import com.cta4j.train.common.model.TrainDirection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents the location of a train on a route.
 *
 * @param run the run number of this train
 * @param destinationStationId the unique identifier of the destination station for this train
 * @param destinationName the display name of the destination station for this train
 * @param direction the direction of travel of this train
 * @param nextStationId the unique identifier of the next station for this train
 * @param nextStopId the unique identifier of the next stop for this train
 * @param nextStationName the display name of the next station for this train
 * @param predictionTime the date and time (UTC) this location was generated
 * @param arrivalTime the date and time (UTC) of this train's arrival at the next stop
 * @param approaching whether this train is currently approaching the next stop
 * @param delayed whether this train is currently delayed
 * @param flags the flags associated with this train, if applicable
 * @param coordinates the current coordinates of this train
 */
@NullMarked
public record LocationTrain(
    String run,

    String destinationStationId,

    String destinationName,

    TrainDirection direction,

    String nextStationId,

    String nextStopId,

    String nextStationName,

    Instant predictionTime,

    Instant arrivalTime,

    boolean approaching,

    boolean delayed,

    @Nullable
    String flags,

    Coordinates coordinates
) {
    /**
     * Constructs a {@code LocationTrain}.
     *
     * @param run the run number of the train
     * @param destinationStationId the unique identifier of the destination station for the train
     * @param destinationName the display name of the destination station for the train
     * @param direction the direction of travel of the train
     * @param nextStationId the unique identifier of the next station for the train
     * @param nextStopId the unique identifier of the next stop for the train
     * @param nextStationName the display name of the next station for the train
     * @param predictionTime the date and time (UTC) the location was generated
     * @param arrivalTime the date and time (UTC) of the train's arrival at the next stop
     * @param approaching whether the train is currently approaching the next stop
     * @param delayed whether the train is currently delayed
     * @param flags the flags associated with the train, if applicable
     * @param coordinates the current coordinates of the train
     * @throws NullPointerException if {@code destinationStationId}, {@code destinationName}, {@code direction},
     * {@code nextStationId}, {@code nextStopId}, {@code nextStationName}, {@code predictionTime},
     * {@code arrivalTime}, or {@code coordinates} is {@code null}
     */
    public LocationTrain {
        Objects.requireNonNull(destinationStationId);
        Objects.requireNonNull(destinationName);
        Objects.requireNonNull(direction);
        Objects.requireNonNull(nextStationId);
        Objects.requireNonNull(nextStopId);
        Objects.requireNonNull(nextStationName);
        Objects.requireNonNull(predictionTime);
        Objects.requireNonNull(arrivalTime);
        Objects.requireNonNull(coordinates);
    }
}
