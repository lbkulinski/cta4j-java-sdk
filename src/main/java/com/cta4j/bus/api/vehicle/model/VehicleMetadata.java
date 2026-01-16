package com.cta4j.bus.api.vehicle.model;

import com.cta4j.bus.model.PassengerLoad;
import com.cta4j.bus.model.TransitMode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

@NullMarked
@SuppressWarnings("ConstantConditions")
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

        if (mode == null) {
            throw new IllegalArgumentException("mode must not be null");
        }

        if (passengerLoad == null) {
            throw new IllegalArgumentException("passengerLoad must not be null");
        }
    }
}
