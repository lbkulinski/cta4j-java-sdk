package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

@NullMarked
@SuppressWarnings("ConstantConditions")
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

    @Nullable
    Boolean delayed,

    ArrivalMetadata metadata
) {
    public Arrival {
        if (stopId == null) {
            throw new IllegalArgumentException("stopId must not be null");
        }

        if (stopName == null) {
            throw new IllegalArgumentException("stopName must not be null");
        }

        if (vehicleId == null) {
            throw new IllegalArgumentException("vehicleId must not be null");
        }

        if (distanceToStop == null) {
            throw new IllegalArgumentException("distanceToStop must not be null");
        }

        if (route == null) {
            throw new IllegalArgumentException("route must not be null");
        }

        if (routeDesignator == null) {
            throw new IllegalArgumentException("routeDesignator must not be null");
        }

        if (routeDirection == null) {
            throw new IllegalArgumentException("routeDirection must not be null");
        }

        if (destination == null) {
            throw new IllegalArgumentException("destination must not be null");
        }

        if (arrivalTime == null) {
            throw new IllegalArgumentException("arrivalTime must not be null");
        }

        if (metadata == null) {
            throw new IllegalArgumentException("metadata must not be null");
        }
    }

    public long etaMinutes() {
        Instant now = Instant.now();

        long minutes = Duration.between(now, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
