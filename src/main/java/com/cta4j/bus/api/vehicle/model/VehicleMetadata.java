package com.cta4j.bus.api.vehicle.model;

import com.cta4j.bus.model.PassengerLoad;
import com.cta4j.bus.model.TransitMode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

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
    public VehicleMetadata(
        @Nullable String dataFeed,
        @Nullable Instant lastUpdated,
        int patternId,
        int distanceToPatternPoint,
        @Nullable Integer stopStatus,
        @Nullable Integer timepointId,
        @Nullable String stopId,
        @Nullable Integer sequence,
        @Nullable Integer gtfsSequence,
        @Nullable Instant serverTimestamp,
        @Nullable Integer speed,
        @Nullable Integer block,
        @Nullable String blockId,
        @Nullable String tripId,
        @Nullable String originalTripNumber,
        @Nullable String zone,
        @Nullable TransitMode mode,
        @Nullable PassengerLoad passengerLoad,
        @Nullable Integer scheduledStartSeconds,
        @Nullable LocalDate scheduledStartDate
    ) {
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

        this.dataFeed = dataFeed;
        this.lastUpdated = lastUpdated;
        this.patternId = patternId;
        this.distanceToPatternPoint = distanceToPatternPoint;
        this.stopStatus = stopStatus;
        this.timepointId = timepointId;
        this.stopId = stopId;
        this.sequence = sequence;
        this.gtfsSequence = gtfsSequence;
        this.serverTimestamp = serverTimestamp;
        this.speed = speed;
        this.block = block;
        this.blockId = blockId;
        this.tripId = tripId;
        this.originalTripNumber = originalTripNumber;
        this.zone = zone;
        this.mode = mode;
        this.passengerLoad = passengerLoad;
        this.scheduledStartSeconds = scheduledStartSeconds;
        this.scheduledStartDate = scheduledStartDate;
    }
}
