package com.cta4j.bus.model;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

public record Arrival(
    PredictionType predictionType,

    String stopId,

    String stopName,

    String vehicleId,

    BigInteger distanceToStop,

    String route,

    String routeDesignator,

    String routeDirection,

    String destination,

    Instant arrivalTime,

    Boolean delayed,

    ArrivalMetadata metadata
) {
    public Long etaMinutes() {
        Instant now = Instant.now();

        long minutes = Duration.between(now, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
