package com.cta4j.bus.vehicle.model;

import com.cta4j.common.geo.Coordinates;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Represents a vehicle.
 *
 * @param id the unique identifier of this vehicle
 * @param route the alphanumeric designator of the route that is currently being serviced by this vehicle
 * @param destination the destination of the trip being serviced by this vehicle (e.g. "Austin")
 * @param coordinates the current coordinates of this vehicle
 * @param delayed whether this vehicle is currently delayed
 * @param metadata the metadata associated with this vehicle
 */
@NullMarked
public record Vehicle(
    String id,

    String route,

    String destination,

    Coordinates coordinates,

    boolean delayed,

    VehicleMetadata metadata
) {
    /**
     * Constructs a {@code Vehicle}.
     *
     * @param id the unique identifier of the vehicle
     * @param route the alphanumeric designator of the route that is currently being serviced by the vehicle
     * @param destination the destination of the trip being serviced by the vehicle (e.g. "Austin")
     * @param coordinates the current coordinates of the vehicle
     * @param delayed whether the vehicle is currently delayed
     * @param metadata the metadata associated with the vehicle
     * @throws NullPointerException if {@code id}, {@code route}, {@code destination}, {@code coordinates}, or
     * {@code metadata} is {@code null}
     */
    public Vehicle {
        Objects.requireNonNull(id);
        Objects.requireNonNull(route);
        Objects.requireNonNull(destination);
        Objects.requireNonNull(coordinates);
        Objects.requireNonNull(metadata);
    }
}
