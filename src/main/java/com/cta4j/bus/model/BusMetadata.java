package com.cta4j.bus.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

public record BusMetadata(
    @Nullable
    String dataFeed,              // rtpidatafeed

    @Nullable
    Instant serverTimestamp,      // srvtmstmp (if you parse it)

    @Nullable
    Integer speed,                // spd

    @Nullable
    Integer patternId,            // pid

    @Nullable
    Integer distanceToPatternPoint, // pdist (or rename if you prefer)

    @Nullable
    Byte stopStatus,              // stopstatus

    @Nullable
    Integer timepointId,          // timepointid

    @Nullable
    String stopId,                // stopid

    @Nullable
    Integer sequence,             // sequence

    @Nullable
    Integer gtfsSequence,         // gtfsseq

    @Nullable
    Integer block,                // blk

    // CTA ops/schedule identifiers
    @Nullable
    String taBlockId,             // tablockid

    @Nullable
    String taTripId,              // tatripid

    @Nullable
    String originalTaTripNo,      // origtatripno

    @Nullable
    String zone,                  // zone

    @Nullable
    TransitMode mode,             // mode

    @Nullable
    PassengerLoad passengerLoad,  // psgld

    // schedule start
    @Nullable
    Integer scheduledStartSeconds, // stst

    @Nullable
    LocalDate scheduledStartDate   // stsd
) {
}
