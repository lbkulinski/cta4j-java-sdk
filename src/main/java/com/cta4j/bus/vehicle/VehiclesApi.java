package com.cta4j.bus.vehicle;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.vehicle.model.Vehicle;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/// Provides access to vehicle-related endpoints of the CTA Bus Tracker API.
///
/// This API allows retrieval of vehicles by their IDs or by associated route IDs.
@NullMarked
public interface VehiclesApi {
    /// Retrieves vehicles by their IDs.
    ///
    /// @param vehicleIds a [Collection] of vehicle IDs
    /// @return a [List] of [Vehicle]s corresponding to the provided IDs, or an empty [List] if no vehicles are found
    /// @throws NullPointerException if `vehicleIds` is `null`, or if any element of `vehicleIds` is `null`
    /// @throws IllegalArgumentException if more than 10 vehicle IDs are provided
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    List<Vehicle> findByIds(Collection<String> vehicleIds);

    /// Retrieves a vehicle by its ID.
    ///
    /// @param vehicleId the vehicle ID
    /// @return an [Optional] containing the [Vehicle] if found, or an empty [Optional] if no vehicle is found for the
    /// given ID
    /// @throws NullPointerException if `vehicleId` is `null`
    /// @throws Cta4jBusException if multiple vehicles are found for the given ID, or if the API returns an error
    /// response or the response cannot be parsed
    default Optional<Vehicle> findById(String vehicleId) {
        Objects.requireNonNull(vehicleId);

        List<String> vehicleIds = List.of(vehicleId);

        List<Vehicle> vehicles = this.findByIds(vehicleIds);

        if (vehicles.isEmpty()) {
            return Optional.empty();
        }

        if (vehicles.size() > 1) {
            String message = "Expected at most one vehicle for ID: %s, but found %d".formatted(
                vehicleId,
                vehicles.size()
            );

            throw new Cta4jBusException(message, BusApiConstants.VEHICLES_ENDPOINT);
        }

        Vehicle vehicle = vehicles.getFirst();

        return Optional.of(vehicle);
    }

    /// Retrieves all vehicles for the specified route IDs.
    ///
    /// @param routeIds a [Collection] of route IDs
    /// @return a [List] of [Vehicle]s associated with the route IDs, or an empty [List] if no vehicles are found for
    /// the route IDs
    /// @throws NullPointerException if `routeIds` is `null`, or if any element of `routeIds` is `null`
    /// @throws IllegalArgumentException if more than 10 route IDs are provided
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    List<Vehicle> findByRouteIds(Collection<String> routeIds);

    /// Retrieves all vehicles for the specified route ID.
    ///
    /// @param routeId the route ID
    /// @return a [List] of [Vehicle]s associated with the route ID, or an empty [List] if no vehicles are found for
    /// the route ID
    /// @throws NullPointerException if `routeId` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    default List<Vehicle> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        List<String> routeIds = List.of(routeId);

        return this.findByRouteIds(routeIds);
    }
}
