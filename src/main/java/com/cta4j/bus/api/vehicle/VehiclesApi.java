package com.cta4j.bus.api.vehicle;

import com.cta4j.bus.api.vehicle.model.Vehicle;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NullMarked
public interface VehiclesApi {
    List<Vehicle> findByIds(@Nullable Collection<@Nullable String> ids);

    default Optional<Vehicle> findById(@Nullable String id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        List<String> ids = List.of(id);

        List<Vehicle> vehicles = this.findByIds(ids);

        if (vehicles.isEmpty()) {
            return Optional.empty();
        } else if (vehicles.size() > 1) {
            String message = String.format("Expected at most one bus for ID: %s, but found %d", id, vehicles.size());

            throw new Cta4jException(message);
        }

        Vehicle vehicle = vehicles.getFirst();

        return Optional.of(vehicle);
    }

    List<Vehicle> findByRouteIds(@Nullable Collection<@Nullable String> routeIds);

    default List<Vehicle> findByRouteId(@Nullable String routeId) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        List<String> routeIds = List.of(routeId);

        return this.findByRouteIds(routeIds);
    }
}
