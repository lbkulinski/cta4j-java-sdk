package com.cta4j.bus.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

public record BusMetadata(
    @Nullable
    String dataFeed,

    @Nullable
    Instant serverTimestamp,

    @Nullable
    Integer speed,

    @Nullable
    Integer patternId,

    @Nullable
    Integer distanceToPatternPoint,

    @Nullable
    Byte stopStatus,

    @Nullable
    Integer timepointId,

    @Nullable
    String stopId,

    @Nullable
    Integer sequence,

    @Nullable
    Integer gtfsSequence,

    @Nullable
    Integer block,

    @Nullable
    String taBlockId,

    @Nullable
    String taTripId,

    @Nullable
    String originalTaTripNo,

    @Nullable
    String zone,

    @Nullable
    TransitMode mode,

    @Nullable
    PassengerLoad passengerLoad,

    @Nullable
    Integer scheduledStartSeconds,

    @Nullable
    LocalDate scheduledStartDate
) {
}
