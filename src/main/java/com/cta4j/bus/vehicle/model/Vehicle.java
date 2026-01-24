package com.cta4j.bus.vehicle.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public record Vehicle(
    String id,

    String route,

    String destination,

    VehicleCoordinates coordinates,

    boolean delayed,

    VehicleMetadata metadata
) {
    public Vehicle {
        Objects.requireNonNull(id);
        Objects.requireNonNull(route);
        Objects.requireNonNull(destination);
        Objects.requireNonNull(coordinates);
        Objects.requireNonNull(metadata);
    }
}
