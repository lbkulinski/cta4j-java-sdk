package com.cta4j.model.bus;

import com.cta4j.external.bus.prediction.CtaPredictionsPrd;
import com.cta4j.util.DateTimeUtils;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record StopArrival(
    BusPredictionType predictionType,

    String stopName,

    int stopId,

    int vehicleId,

    BigInteger distanceToStop,

    String route,

    String routeDesignator,

    String routeDirection,

    String destination,

    Instant arrivalTime,

    boolean delayed
) {
    public static StopArrival fromExternal(CtaPredictionsPrd prd) {
        Objects.requireNonNull(prd);

        return new StopArrival(
            BusPredictionType.valueOf(prd.typ()),
            prd.stpnm(),
            Integer.parseInt(prd.stpid()),
            Integer.parseInt(prd.vid()),
            BigInteger.valueOf(prd.dstp()),
            prd.rt(),
            prd.rtdd(),
            prd.rtdir(),
            prd.des(),
            DateTimeUtils.parseBusTimestamp(prd.prdtm()),
            prd.dly()
        );
    }
    public Long etaMinutes() {
        Instant now = Instant.now();

        long minutes = Duration.between(now, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
