package com.cta4j.bus.api;

import com.cta4j.bus.api.detour.DetoursApi;
import com.cta4j.bus.api.direction.DirectionsApi;
import com.cta4j.bus.api.impl.BusApiImpl;
import com.cta4j.bus.api.locale.LocalesApi;
import com.cta4j.bus.api.pattern.PatternsApi;
import com.cta4j.bus.api.prediction.PredictionsApi;
import com.cta4j.bus.api.route.RoutesApi;
import com.cta4j.bus.api.stop.StopsApi;
import com.cta4j.bus.api.vehicle.VehiclesApi;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.Objects;

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
         * @throws NullPointerException if {@code host} is {@code null}
         */
        Builder host(String host);

        /**
         * Builds a configured {@link BusApi} instance.
         *
         * @return a new {@link BusApi}
         */
        BusApi build();
    }

    /**
     * Creates a new {@link Builder} for configuring a {@link BusApi} instance.
     *
     * @param apiKey the API key
     * @return a new {@link Builder}
     * @throws NullPointerException if {@code apiKey} is {@code null}
     */
    static Builder builder(String apiKey) {
        Objects.requireNonNull(apiKey);

        return new BusApiImpl.BuilderImpl(apiKey);
    }
}
