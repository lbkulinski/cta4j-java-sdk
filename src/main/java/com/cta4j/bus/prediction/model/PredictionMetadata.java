package com.cta4j.bus.prediction.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents metadata associated with a bus arrival prediction.
 *
 * @param timestamp the date and time (UTC) this prediction was generated
 * @param dynamicAction the {@link DynamicAction} affecting this prediction
 * @param blockId the scheduled block identifier for the vehicle associated with this prediction
 * @param tripId the scheduled trip identifier for the vehicle associated with this prediction
 * @param originalTripNumber the trip identifier for the vehicle associated with this prediction
 * @param zone the zone name for the vehicle associated with this prediction, otherwise blank
 * @param passengerLoad the {@link PassengerLoad} of the vehicle associated with this prediction
 * @param gtfsSequence the GTFS sequence number associated with this prediction, if applicable
 * @param nextBus the next bus identifier associated with this prediction, if applicable
 * @param scheduledStartSeconds the scheduled start time in seconds past midnight associated with this prediction, if
 *                              applicable
 * @param scheduledStartDate the scheduled start date associated with this prediction, if applicable
 * @param flagStop the {@link FlagStop} information of the vehicle associated with this prediction
 */
@NullMarked
public record PredictionMetadata(
    Instant timestamp,

    DynamicAction dynamicAction,

    String blockId,

    String tripId,

    String originalTripNumber,

    String zone,

    PassengerLoad passengerLoad,

    @Nullable
    Integer gtfsSequence,

    @Nullable
    String nextBus,

    @Nullable
    Integer scheduledStartSeconds,

    @Nullable
    LocalDate scheduledStartDate,

    FlagStop flagStop
) {
    /**
     * Constructs a {@code PredictionMetadata}.
     *
     * @param timestamp the date and time (UTC) the prediction was generated
     * @param dynamicAction the {@link DynamicAction} affecting the prediction
     * @param blockId the scheduled block identifier for the vehicle associated with the prediction
     * @param tripId the scheduled trip identifier for the vehicle associated with the prediction
     * @param originalTripNumber the trip identifier for the vehicle associated with the prediction
     * @param zone the zone name for the vehicle associated with the prediction, otherwise blank
     * @param passengerLoad the {@link PassengerLoad} of the vehicle associated with the prediction
     * @param gtfsSequence the GTFS sequence number associated with the prediction, if applicable
     * @param nextBus the next bus identifier associated with the prediction, if applicable
     * @param scheduledStartSeconds the scheduled start time in seconds past midnight associated with the prediction,
     *                              if applicable
     * @param scheduledStartDate the scheduled start date associated with the prediction, if applicable
     * @param flagStop the {@link FlagStop} information of the vehicle associated with the prediction
     * @throws NullPointerException if {@code timestamp}, {@code dynamicAction}, {@code blockId}, {@code tripId},
     * {@code originalTripNumber}, {@code zone}, {@code passengerLoad}, or {@code flagStop} is {@code null}
     */
    public PredictionMetadata {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(dynamicAction);
        Objects.requireNonNull(blockId);
        Objects.requireNonNull(tripId);
        Objects.requireNonNull(originalTripNumber);
        Objects.requireNonNull(zone);
        Objects.requireNonNull(passengerLoad);
        Objects.requireNonNull(flagStop);
    }
}
