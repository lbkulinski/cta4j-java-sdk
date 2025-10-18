package com.cta4j.bus.client;

import com.cta4j.bus.client.internal.BusClientImpl;
import com.cta4j.bus.model.*;
import com.cta4j.exception.Cta4jException;

import java.util.List;
import java.util.Optional;

/**
 * A client for interacting with the CTA Bus Tracker API.
 */
public interface BusClient {
    /**
     * Retrieves a {@link List} of all bus routes.
     *
     * @return a {@link List} of all bus routes
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Route> getRoutes();

    /**
     * Retrieves a {@link List} of directions for a specific bus route.
     *
     * @param routeId the ID of the bus route
     * @return a {@link List} of directions for the specified bus route
     * @throws NullPointerException if the specified bus route is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<String> getDirections(String routeId);

    /**
     * Retrieves a {@link List} of stops for a specific bus route and direction.
     *
     * @param routeId the ID of the bus route
     * @param direction the direction of the bus route
     * @return a {@link List} of stops for the specified bus route and direction
     * @throws NullPointerException if the specified bus route or direction is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Stop> getStops(String routeId, String direction);

    /**
     * Retrieves a {@link List} of upcoming arrivals for a specific bus route and stop.
     *
     * @param routeId the ID of the bus route
     * @param stopId the ID of the bus stop
     * @return a {@link List} of upcoming arrivals for the specified bus route and stop
     * @throws NullPointerException if the specified bus route or stop is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<StopArrival> getStopArrivals(String routeId, String stopId);

    /**
     * Retrieves a {@link List} of detours for a specific bus route and direction.
     *
     * @param routeId the ID of the bus route
     * @param direction the direction of the bus route
     * @return a {@link List} of detours for the specified bus route and direction
     * @throws NullPointerException if the specified bus route or direction is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<Detour> getDetours(String routeId, String direction);

    /**
     * Retrieves information about a specific bus by its ID.
     *
     * @param id the ID of the bus
     * @return an {@link Optional} containing the bus information if found, or an empty {@link Optional} if not found
     * @throws NullPointerException if the specified bus ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    Optional<Bus> getBus(String id);

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
         * @throws NullPointerException if {@code host} is {@code null}
         */
        Builder host(String host);

        /**
         * Sets the API key used for authentication.
         *
         * @param apiKey the API key
         * @return this {@link Builder} for method chaining
         * @throws NullPointerException if {@code apiKey} is {@code null}
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
