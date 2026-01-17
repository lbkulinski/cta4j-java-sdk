package com.cta4j.bus.api.vehicle.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record Vehicle(
    String id,

    String route,

    String destination,

    VehicleCoordinates coordinates,

    boolean delayed,

    VehicleMetadata metadata
) {
    public Vehicle(
        @Nullable String id,
        @Nullable String route,
        @Nullable String destination,
        @Nullable VehicleCoordinates coordinates,
        boolean delayed,
        @Nullable VehicleMetadata metadata
    ) {
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

        this.id = id;
        this.route = route;
        this.destination = destination;
        this.coordinates = coordinates;
        this.delayed = delayed;
        this.metadata = metadata;
    }
}
