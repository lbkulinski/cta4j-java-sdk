package com.cta4j.bus.prediction.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Represents a bus arrival prediction.
 *
 * @param predictionType the type of this prediction
 * @param stopId the unique identifier of the stop for which this prediction was generated
 * @param stopName the display name of the stop for which this prediction was generated
 * @param vehicleId the unique identifier of the vehicle for which this prediction was generated
 * @param distanceToStop the feet left to be traveled by the vehicle before it reaches the stop associated with this
 *                       prediction
 * @param route the alphanumeric designator of the route (e.g. "20" or "X20") for which this prediction was generated
 * @param routeDesignator the language-specific route designator of this prediction, intended for display
 * @param routeDirection the direction of travel of the route associated with this prediction (e.g. "Eastbound")
 * @param destination the final destination of the vehicle associated with this prediction
 * @param arrivalTime the predicted date and time (UTC) of a vehicle’s arrival or departure to the stop associated with
 *                    this prediction
 * @param delayed whether the vehicle associated with this prediction is currently delayed
 * @param metadata the metadata associated with this prediction
 */
@NullMarked
public record Prediction(
    PredictionType predictionType,

    String stopId,

    String stopName,

    String vehicleId,

    BigInteger distanceToStop,

    String route,

    String routeDesignator,

    String routeDirection,

    String destination,

    Instant arrivalTime,

    @Nullable
    Boolean delayed,

    PredictionMetadata metadata
) {
    /**
     * Constructs a {@code Prediction}.
     *
     * @param predictionType the type of the prediction
     * @param stopId the unique identifier of the stop for which the prediction was generated
     * @param stopName the display name of the stop for which the prediction was generated
     * @param vehicleId the unique identifier of the vehicle for which the prediction was generated
     * @param distanceToStop the feet left to be traveled by the vehicle before it reaches the stop associated with the
     *                       prediction
     * @param route the alphanumeric designator of the route (e.g. "20" or "X20") for which the prediction was
     *              generated
     * @param routeDesignator the language-specific route designator of the prediction, intended for display
     * @param routeDirection the direction of travel of the route associated with the prediction (e.g. "Eastbound")
     * @param destination the final destination of the vehicle associated with the prediction
     * @param arrivalTime the predicted date and time (UTC) of a vehicle’s arrival or departure to the stop associated
     *                    with the prediction
     * @param delayed whether the vehicle associated with the prediction is currently delayed
     * @param metadata the metadata associated with the prediction
     * @throws NullPointerException if {@code predictionType}, {@code stopId}, {@code stopName}, {@code vehicleId},
     * {@code distanceToStop}, {@code route}, {@code routeDesignator}, {@code routeDirection}, {@code destination},
     * {@code arrivalTime}, or {@code metadata} is {@code null}
     */
    public Prediction {
        Objects.requireNonNull(predictionType);
        Objects.requireNonNull(stopId);
        Objects.requireNonNull(stopName);
        Objects.requireNonNull(vehicleId);
        Objects.requireNonNull(distanceToStop);
        Objects.requireNonNull(route);
        Objects.requireNonNull(routeDesignator);
        Objects.requireNonNull(routeDirection);
        Objects.requireNonNull(destination);
        Objects.requireNonNull(arrivalTime);
        Objects.requireNonNull(metadata);
    }

    /**
     * Calculates the estimated time of arrival (ETA) in minutes from the current time to the predicted arrival time.
     *
     * @return the ETA in minutes; returns 0 if the predicted arrival time is in the past
     */
    public long etaMinutes() {
        Instant now = Instant.now();

        long minutes = Duration.between(now, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
