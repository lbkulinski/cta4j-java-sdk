package com.cta4j.bus.vehicle;

import com.cta4j.bus.vehicle.model.Vehicle;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NullMarked
public interface VehiclesApi {
    List<Vehicle> findByIds(Collection<String> ids);

    default Optional<Vehicle> findById(String id) {
        Objects.requireNonNull(id);

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

    List<Vehicle> findByRouteIds(Collection<String> routeIds);

    default List<Vehicle> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        List<String> routeIds = List.of(routeId);

        return this.findByRouteIds(routeIds);
    }
}
