package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

@NullMarked
@SuppressWarnings("ConstantConditions")
public record ArrivalMetadata(
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
    public ArrivalMetadata {
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }

        if (blockId == null) {
            throw new IllegalArgumentException("blockId must not be null");
        }

        if (tripId == null) {
            throw new IllegalArgumentException("tripId must not be null");
        }

        if (originalTripNumber == null) {
            throw new IllegalArgumentException("originalTripNumber must not be null");
        }

        if (zone == null) {
            throw new IllegalArgumentException("zone must not be null");
        }

        if (passengerLoad == null) {
            throw new IllegalArgumentException("passengerLoad must not be null");
        }
    }
}
