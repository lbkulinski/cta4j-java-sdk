package com.cta4j.bus.client;

import com.cta4j.bus.client.internal.BusClientImpl;
import com.cta4j.bus.model.Arrival;
import com.cta4j.bus.api.vehicle.model.Vehicle;
import com.cta4j.bus.model.Detour;
import com.cta4j.bus.api.route.model.Route;
import com.cta4j.bus.model.RoutePattern;
import com.cta4j.bus.model.Stop;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * A client for interacting with the CTA Bus Tracker API.
 */
@NullMarked
@SuppressWarnings("ConstantConditions")
public interface BusClient {
    /**
     * Retrieves the current system time from the CTA Bus Tracker API.
     *
     * @return the current system time as an {@link Instant}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    Instant getSystemTime();

    /**
     * Finds buses matching the specified bus IDs.
     *
     * @param ids an {@link Iterable} of bus IDs
     * @return a {@link List} of buses corresponding to the specified IDs
     * @throws IllegalArgumentException if the specified IDs are {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Vehicle> findBusesById(Iterable<String> ids);

    /**
     * Finds buses operating on the specified route IDs.
     *
     * @param routeIds an {@link Iterable} of bus route IDs
     * @return a {@link List} of buses corresponding to the specified route IDs
     * @throws IllegalArgumentException if the specified route IDs are {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Vehicle> findBusesByRouteId(Iterable<String> routeIds);

    /**
     * Finds buses operating on the specified route ID.
     *
     * @param routeId the bus route ID
     * @return a {@link List} of buses corresponding to the specified route ID
     * @throws IllegalArgumentException if the specified route ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    default List<Vehicle> findBusesByRouteId(String routeId) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        List<String> routeIds = List.of(routeId);

        return this.findBusesByRouteId(routeIds);
    }

    /**
     * Finds a bus by its ID.
     *
     * @param id the ID of the bus
     * @return an {@link Optional} containing the bus information if found, or an empty {@link Optional} if not found
     * @throws IllegalArgumentException if the specified bus ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    default Optional<Vehicle> findBusById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        List<String> ids = List.of(id);

        List<Vehicle> vehicles = this.findBusesById(ids);

        if (vehicles.isEmpty()) {
            return Optional.empty();
        } if (vehicles.size() > 1) {
            String message = String.format("Multiple buses found for ID: %s", id);

            throw new Cta4jException(message);
        }

        Vehicle vehicle = vehicles.getFirst();

        return Optional.of(vehicle);
    }

    /**
     * Retrieves a {@link List} of all bus routes.
     *
     * @return a {@link List} of all bus routes
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Route> getRoutes();

    /**
     * Finds directions for the specified route ID.
     *
     * @param routeId the ID of the bus route
     * @return a {@link List} of directions for the specified bus route
     * @throws IllegalArgumentException if the specified bus route is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<String> findDirectionsByRouteId(String routeId);

    /**
     * Finds stops for the specified route ID and direction.
     *
     * @param routeId the ID of the bus route
     * @param direction the direction of the bus route
     * @return a {@link List} of stops for the specified bus route and direction
     * @throws IllegalArgumentException if the specified bus route or direction is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Stop> findStopsByRouteIdAndDirection(String routeId, String direction);

    /**
     * Finds stops for the specified stop IDs.
     *
     * @param stopIds an {@link Iterable} of stop IDs
     * @return a {@link List} of stops corresponding to the specified stop IDs
     * @throws IllegalArgumentException if the specified stop IDs are {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Stop> findStopsByStopId(Iterable<String> stopIds);

    /**
     * Finds a stop by its ID.
     *
     * @param stopId the ID of the stop
     * @return an {@link Optional} containing the stop information if found, or an empty {@link Optional} if not found
     * @throws IllegalArgumentException if the specified stop ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    default Optional<Stop> findStopsByStopId(String stopId) {
        if (stopId == null) {
            throw new IllegalArgumentException("stopId must not be null");
        }

        List<String> ids = List.of(stopId);

        List<Stop> stops = this.findStopsByStopId(ids);

        if (stops.isEmpty()) {
            return Optional.empty();
        } if (stops.size() > 1) {
            String message = String.format("Multiple stops found for ID: %s", stopId);

            throw new Cta4jException(message);
        }

        Stop stop = stops.getFirst();

        return Optional.of(stop);
    }

    /**
     * Finds route patterns for the specified pattern IDs.
     *
     * @param patternIds an {@link Iterable} of route pattern IDs
     * @return a {@link List} of route patterns corresponding to the specified pattern IDs
     * @throws IllegalArgumentException if the specified pattern IDs are {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<RoutePattern> findPatternsByPatternId(Iterable<String> patternIds);

    /**
     * Finds route patterns for the specified pattern ID.
     *
     * @param patternId the ID of the route pattern
     * @return a {@link List} of route patterns corresponding to the specified pattern ID
     * @throws IllegalArgumentException if the specified pattern ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    default List<RoutePattern> findPatternsByPatternId(String patternId) {
        if (patternId == null) {
            throw new IllegalArgumentException("patternId must not be null");
        }

        List<String> ids = List.of(patternId);

        return this.findPatternsByPatternId(ids);
    }

    /**
     * Finds route patterns for the specified route ID.
     *
     * @param routeId the ID of the bus route
     * @return a {@link List} of route patterns for the specified bus route
     * @throws IllegalArgumentException if the specified bus route is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<RoutePattern> findPatternsByRouteId(String routeId);

    /**
     * Finds arrivals for the specified route ID and stop ID.
     *
     * @param routeId the ID of the bus route
     * @param stopId the ID of the bus stop
     * @return a {@link List} of arrivals for the specified bus route and stop
     * @throws IllegalArgumentException if the specified bus route or stop is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Arrival> findArrivalsByRouteIdAndStopId(String routeId, String stopId);

    /**
     * Finds arrivals for the specified bus ID.
     *
     * @param busId the ID of the bus
     * @return a {@link List} of arrivals for the specified bus
     * @throws IllegalArgumentException if the specified bus ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Arrival> findArrivalsByBusId(String busId);

    /**
     * Finds detours for the specified route ID and direction.
     *
     * @param routeId the ID of the bus route
     * @param direction the direction of the bus route
     * @return a {@link List} of detours for the specified bus route and direction
     * @throws IllegalArgumentException if the specified bus route or direction is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Detour> findDetoursByRouteIdAndDirection(String routeId, String direction);

    /**
     * A builder for configuring and creating {@link BusClient} instances.
     *
     * <p>Fluent, non-thread-safe builder. Call {@link #build()} to obtain a configured client.
     */
    interface Builder {
        /**
         * Sets the API host used by the client.
         *
         * @param host the host
         * @return this {@link Builder} for method chaining
         * @throws IllegalArgumentException if {@code host} is {@code null}
         */
        Builder host(String host);

        /**
         * Sets the API key used for authentication.
         *
         * @param apiKey the API key
         * @return this {@link Builder} for method chaining
         * @throws IllegalArgumentException if {@code apiKey} is {@code null}
         */
        Builder apiKey(String apiKey);

        /**
         * Builds a configured {@link BusClient} instance.
         *
         * @return a new {@link BusClient}
         */
        BusClient build();
    }

    /**
     * Creates a new {@link Builder} for configuring and building a {@link BusClient}.
     *
     * @return a new {@link Builder} instance
     */
    static Builder builder() {
        return new BusClientImpl.BuilderImpl();
    }
}
