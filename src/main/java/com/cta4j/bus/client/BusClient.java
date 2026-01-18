package com.cta4j.bus.client;

import com.cta4j.bus.client.internal.BusClientImpl;
import com.cta4j.bus.model.Detour;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * A client for interacting with the CTA Bus Tracker API.
 */
@NullMarked
@SuppressWarnings("ConstantConditions")
public interface BusClient {
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
