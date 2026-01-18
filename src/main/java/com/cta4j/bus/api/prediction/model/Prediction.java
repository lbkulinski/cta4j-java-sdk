package com.cta4j.bus.api.prediction.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@NullMarked
public record Prediction(
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

    PredictionMetadata metadata
) {
    public Prediction {
        Objects.requireNonNull(predictionType);
        Objects.requireNonNull(stopId);
        Objects.requireNonNull(stopName);
        Objects.requireNonNull(vehicleId);
        Objects.requireNonNull(distanceToStop);
        Objects.requireNonNull(route);
        Objects.requireNonNull(routeDesignator);
        Objects.requireNonNull(routeDirection);
        Objects.requireNonNull(destination);
        Objects.requireNonNull(arrivalTime);
        Objects.requireNonNull(metadata);
    }

    public long etaMinutes() {
        Instant now = Instant.now();

        long minutes = Duration.between(now, this.arrivalTime)
                               .toMinutes();

        return Math.max(minutes, 0L);
    }
}
