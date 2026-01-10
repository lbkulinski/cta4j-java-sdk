package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("ConstantConditions")
public record Bus(
    String id,

    String route,

    String destination,

    BusCoordinates coordinates,

    boolean delayed,

    BusMetadata metadata
) {
    public Bus {
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
