package com.cta4j.bus.api.vehicle.model;

import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("ConstantConditions")
public record Vehicle(
    String id,

    String route,

    String destination,

    VehicleCoordinates coordinates,

    boolean delayed,

    VehicleMetadata metadata
) {
    public Vehicle {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        if (route == null) {
            throw new IllegalArgumentException("route must not be null");
        }

        if (destination == null) {
            throw new IllegalArgumentException("destination must not be null");
        }

        if (coordinates == null) {
            throw new IllegalArgumentException("coordinates must not be null");
        }

        if (metadata == null) {
            throw new IllegalArgumentException("metadata must not be null");
        }
    }
}
