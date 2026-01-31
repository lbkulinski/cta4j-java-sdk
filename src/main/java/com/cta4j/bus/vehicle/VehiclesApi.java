package com.cta4j.bus.vehicle;

import com.cta4j.bus.vehicle.model.Vehicle;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides access to vehicle-related endpoints of the CTA BusTime API.
 * <p>
 * This API allows retrieval of vehicles by their IDs or by associated route IDs.
 */
@NullMarked
public interface VehiclesApi {
    /**
     * Retrieves vehicles by their IDs.
     *
     * @param ids a {@link Collection} of vehicle IDs
     * @return a {@link List} of {@link Vehicle}s corresponding to the provided IDs, or an empty {@link List} if no
     * vehicles are found
     * @throws NullPointerException if {@code ids} is {@code null} or contains {@code null} elements
     */
    List<Vehicle> findByIds(Collection<String> ids);

    /**
     * Retrieves a vehicle by its ID.
     *
     * @param id the vehicle ID
     * @return an {@link Optional} containing the {@link Vehicle} if found, or an empty {@link Optional} if no vehicle
     * exists with the given ID
     * @throws NullPointerException if {@code id} is {@code null}
     * @throws Cta4jException if multiple vehicles are found for the given ID
     */
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

    /**
     * Retrieves all vehicles for the specified route IDs.
     *
     * @param routeIds a {@link Collection} of route IDs
     * @return a {@link List} of {@link Vehicle}s associated with the route IDs, or an empty {@link List} if no
     * vehicles exist for the route IDs
     * @throws NullPointerException if {@code routeIds} is {@code null} or contains {@code null} elements
     */
    List<Vehicle> findByRouteIds(Collection<String> routeIds);

    /**
     * Retrieves all vehicles for the specified route ID.
     *
     * @param routeId the route ID
     * @return a {@link List} of {@link Vehicle}s associated with the route ID, or an empty {@link List} if no vehicles
     * exist for the route ID
     * @throws NullPointerException if {@code routeId} is {@code null}
     */
    default List<Vehicle> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        List<String> routeIds = List.of(routeId);

        return this.findByRouteIds(routeIds);
    }
}
