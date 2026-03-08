package com.cta4j.train.common.model;

import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a train arrival.
 *
 * @param stationId the unique identifier of the station for which this arrival was generated
 * @param stationName the display name of the station for which this arrival was generated
 * @param stopId the unique identifier of the stop for which this arrival was generated
 * @param stopDescription the display name of the stop for which this arrival was generated
 * @param line the train line associated with this arrival
 * @param destinationStationId the unique identifier of the destination station for this arrival
 * @param destinationName the display name of the destination station for this arrival
 * @param predictionTime the date and time (UTC) this arrival was generated
 * @param arrivalTime the date and time (UTC) of a train’s arrival or departure to the stop associated with this
 *                    arrival
 * @param approaching whether the train associated with this arrival is currently approaching the stop
 * @param scheduled whether this arrival is based on a scheduled time rather than a real-time prediction
 * @param delayed whether the train associated with this arrival is currently delayed
 * @param fault whether the train associated with this arrival is currently experiencing a fault
 * @param metadata the metadata associated with this arrival
 */
@NullMarked
public record Arrival(
    String stationId,

    String stationName,

    String stopId,

    String stopDescription,

    TrainLine line,

    String destinationStationId,

    String destinationName,

    Instant predictionTime,

    Instant arrivalTime,

    boolean approaching,

    boolean scheduled,

    boolean delayed,

    boolean fault,

    ArrivalMetadata metadata
) {
    /**
     * Constructs an {@code Arrival}.
     *
     * @param stationId the unique identifier of the station for which the arrival was generated
     * @param stationName the display name of the station for which the arrival was generated
     * @param stopId the unique identifier of the stop for which the arrival was generated
     * @param stopDescription the display name of the stop for which the arrival was generated
     * @param line the train line associated with the arrival
     * @param destinationStationId the unique identifier of the destination station for the arrival
     * @param destinationName the display name of the destination station for the arrival
     * @param predictionTime the date and time (UTC) the arrival was generated
     * @param arrivalTime the date and time (UTC) of a train’s arrival or departure to the stop associated with the
     *                    arrival
     * @param approaching whether the train associated with the arrival is currently approaching the stop
     * @param scheduled whether the arrival is based on a scheduled time rather than a real-time prediction
     * @param delayed whether the train associated with the arrival is currently delayed
     * @param fault whether the train associated with the arrival is currently experiencing a fault
     * @param metadata the metadata associated with the arrival
     * @throws NullPointerException if {@code stationId}, {@code stationName}, {@code stopId}, {@code stopDescription},
     * {@code line}, {@code destinationStationId}, {@code destinationName}, {@code predictionTime},
     * {@code arrivalTime}, or {@code metadata} is {@code null}
     */
    public Arrival {
        Objects.requireNonNull(stationId);
        Objects.requireNonNull(stationName);
        Objects.requireNonNull(stopId);
        Objects.requireNonNull(stopDescription);
        Objects.requireNonNull(line);
        Objects.requireNonNull(destinationStationId);
        Objects.requireNonNull(destinationName);
        Objects.requireNonNull(predictionTime);
        Objects.requireNonNull(arrivalTime);
        Objects.requireNonNull(metadata);
    }
}
