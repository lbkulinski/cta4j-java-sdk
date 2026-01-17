package com.cta4j.bus.api;

import com.cta4j.bus.api.impl.BusApiImpl;
import com.cta4j.bus.api.route.RoutesApi;
import com.cta4j.bus.api.vehicle.VehiclesApi;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;

@NullMarked
public interface BusApi {
    Instant systemTime();

    VehiclesApi vehicles();

    RoutesApi routes();

    DirectionsApi directions();

    StopsApi stops();

    PatternsApi patterns();

    PredictionsApi predictions();

    LocalesApi locales();

    DetoursApi detours();

    /**
     * A builder for configuring and creating {@link BusApi} instances.
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
         * Builds a configured {@link BusApi} instance.
         *
         * @return a new {@link BusApi}
         */
        BusApi build();
    }

    /**
     * Creates a new {@link Builder} for configuring and building a {@link BusApi}.
     *
     * @return a new {@link Builder} instance
     */
    static Builder builder() {
        return new BusApiImpl.BuilderImpl();
    }
}
