package com.cta4j.model.train;

import com.cta4j.external.train.arrival.CtaArrivalsEta;
import com.cta4j.util.DateTimeUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record StationArrival(
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

    String flags,

    BigDecimal latitude,

    BigDecimal longitude,

    int heading
) {
    public static StationArrival fromExternal(CtaArrivalsEta eta) {
        Objects.requireNonNull(eta);

        return new StationArrival(
            eta.staId(),
            eta.stpId(),
            eta.staNm(),
            eta.stpDe(),
            eta.rn(),
            Route.parseString(eta.rt()),
            eta.destSt(),
            eta.destNm(),
            Integer.parseInt(eta.trDr()),
            DateTimeUtils.parseTrainTimestamp(eta.prdt()),
            DateTimeUtils.parseTrainTimestamp(eta.arrT()),
            "1".equals(eta.isApp()),
            "1".equals(eta.isSch()),
            "1".equals(eta.isDly()),
            "1".equals(eta.isFlt()),
            eta.flags(),
            new BigDecimal(eta.lat()),
            new BigDecimal(eta.lon()),
            Integer.parseInt(eta.heading())
        );
    }

    public long etaMinutes() {
        long minutes = Duration.between(this.predictionTime, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
