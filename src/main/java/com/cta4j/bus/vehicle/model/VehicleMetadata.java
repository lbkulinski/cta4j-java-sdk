package com.cta4j.bus.vehicle.model;

import com.cta4j.bus.prediction.model.PassengerLoad;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@NullMarked
public record VehicleMetadata(
    @Nullable
    String dataFeed,

    @Nullable
    Instant lastUpdated,

    int patternId,

    int distanceToPatternPoint,

    @Nullable
    Integer stopStatus,

    @Nullable
    Integer timepointId,

    @Nullable
    String stopId,

    @Nullable
    Integer sequence,

    @Nullable
    Integer gtfsSequence,

    @Nullable
    Instant serverTimestamp,

    @Nullable
    Integer speed,

    @Nullable
    Integer block,

    String blockId,

    String tripId,

    String originalTripNumber,

    String zone,

    TransitMode mode,

    PassengerLoad passengerLoad,

    @Nullable
    Integer scheduledStartSeconds,

    @Nullable
    LocalDate scheduledStartDate
) {
    public VehicleMetadata {
        Objects.requireNonNull(blockId);
        Objects.requireNonNull(tripId);
        Objects.requireNonNull(originalTripNumber);
        Objects.requireNonNull(zone);
        Objects.requireNonNull(mode);
        Objects.requireNonNull(passengerLoad);
    }
}
