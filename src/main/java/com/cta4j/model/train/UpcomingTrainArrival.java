package com.cta4j.model.train;

import com.cta4j.external.train.follow.CtaFollowEta;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record UpcomingTrainArrival(
    String stationId,

    String stopId,

    String stationName,

    String stopDescription,

    String run,

    Route route,

    String destinationStopId,

    String destinationName,

    int direction,

    Instant predictionTime,

    Instant arrivalTime,

    boolean approaching,

    boolean scheduled,

    boolean delayed,

    boolean faulted,

    String flags
) {
    public static UpcomingTrainArrival fromExternal(CtaFollowEta eta) {
        Objects.requireNonNull(eta);

        return new UpcomingTrainArrival(
            eta.staId(),
            eta.stpId(),
            eta.staNm(),
            eta.stpDe(),
            eta.rn(),
            Route.parseString(eta.rt()),
            eta.destSt(),
            eta.destNm(),
            Integer.parseInt(eta.trDr()),
            Instant.ofEpochMilli(Long.parseLong(eta.prdt())),
            Instant.ofEpochMilli(Long.parseLong(eta.arrT())),
            "1".equals(eta.isApp()),
            "1".equals(eta.isSch()),
            "1".equals(eta.isDly()),
            "1".equals(eta.isFlt()),
            eta.flags()
        );
    }
    public long etaMinutes() {
        long minutes = Duration.between(this.predictionTime, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
