package com.cta4j.model.train;

import com.cta4j.external.train.follow.CtaFollowEta;
import com.cta4j.model.common.TrainRoute;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record UpcomingTrainArrival(
    int stationId,

    int stopId,

    String stationName,

    String stopDescription,

    int run,

    TrainRoute route,

    int destinationStopId,

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
            Integer.parseInt(eta.staId()),
            Integer.parseInt(eta.stpId()),
            eta.staNm(),
            eta.stpDe(),
            Integer.parseInt(eta.rn()),
            TrainRoute.parseString(eta.rt()),
            Integer.parseInt(eta.destSt()),
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
