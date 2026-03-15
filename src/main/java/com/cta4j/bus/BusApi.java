package com.cta4j.bus;

import com.cta4j.bus.detour.DetoursApi;
import com.cta4j.bus.direction.DirectionsApi;
import com.cta4j.bus.common.internal.impl.BusApiImpl;
import com.cta4j.bus.locale.LocalesApi;
import com.cta4j.bus.pattern.PatternsApi;
import com.cta4j.bus.prediction.PredictionsApi;
import com.cta4j.bus.route.RoutesApi;
import com.cta4j.bus.stop.StopsApi;
import com.cta4j.bus.vehicle.VehiclesApi;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.Objects;

/**
 * Primary entry point for interacting with the CTA Bus Tracker API.
 * <p>
 * This interface provides access to the current system time as well as
 * grouped sub-APIs for vehicles, routes, directions, stops, patterns,
 * predictions, locales, and detours.
 * <p>
 * Instances of {@code BusApi} are immutable and thread-safe once built.
 * Use {@link #builder(String)} to construct a configured instance.
 */
@NullMarked
public interface BusApi {
    /**
     * Returns the current system time reported by the Bus Tracker API.
     *
     * @return the API system time as an {@link Instant}
     */
    Instant systemTime();

    /**
     * Provides access to vehicle-related endpoints.
     *
     * @return the {@link VehiclesApi}
     */
    VehiclesApi vehicles();

    /**
     * Provides access to route-related endpoints.
     *
     * @return the {@link RoutesApi}
     */
    RoutesApi routes();

    /**
     * Provides access to direction-related endpoints.
     *
     * @return the {@link DirectionsApi}
     */
    DirectionsApi directions();

    /**
     * Provides access to stop-related endpoints.
     *
     * @return the {@link StopsApi}
     */
    StopsApi stops();

    /**
     * Provides access to route pattern–related endpoints.
     *
     * @return the {@link PatternsApi}
     */
    PatternsApi patterns();

    /**
     * Provides access to prediction and arrival-related endpoints.
     *
     * @return the {@link PredictionsApi}
     */
    PredictionsApi predictions();

    /**
     * Provides access to locale and language-related endpoints.
     *
     * @return the {@link LocalesApi}
     */
    LocalesApi locales();

    /**
     * Provides access to detour-related endpoints.
     *
     * @return the {@link DetoursApi}
     */
    DetoursApi detours();

    /**
     * Builder for constructing {@link BusApi} instances.
     */
    interface Builder {
        /**
         * Sets the API host to use for requests.
         * <p>
         * If not specified, the default CTA Bus Tracker API host is used.
         *
         * @param host the API host
         * @return this builder instance
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
     * Creates a new {@link Builder} for constructing a {@link BusApi}.
     *
     * @param apiKey the CTA Bus Tracker API key
     * @return a new {@link Builder}
     * @throws NullPointerException if {@code apiKey} is {@code null}
     */
    static Builder builder(String apiKey) {
        Objects.requireNonNull(apiKey);

        return new BusApiImpl.BuilderImpl(apiKey);
    }
}
