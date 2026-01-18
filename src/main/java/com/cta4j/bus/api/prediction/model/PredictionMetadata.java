package com.cta4j.bus.api.prediction.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

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
    String scheduledStartDate,

    FlagStop flagStop
) {
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
