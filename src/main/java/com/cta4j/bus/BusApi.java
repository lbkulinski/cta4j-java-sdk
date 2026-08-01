package com.cta4j.bus;

import com.cta4j.bus.common.internal.impl.BusApiImpl;
import com.cta4j.bus.detour.DetoursApi;
import com.cta4j.bus.direction.DirectionsApi;
import com.cta4j.bus.locale.LocalesApi;
import com.cta4j.bus.pattern.PatternsApi;
import com.cta4j.bus.prediction.PredictionsApi;
import com.cta4j.bus.route.RoutesApi;
import com.cta4j.bus.stop.StopsApi;
import com.cta4j.bus.vehicle.VehiclesApi;
import com.cta4j.common.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.Objects;

/// Primary entry point for interacting with the CTA Bus Tracker API.
///
/// This interface provides access to the current system time as well as grouped sub-APIs for vehicles, routes,
/// directions, stops, patterns, predictions, locales, and detours.
///
/// Instances of `BusApi` are immutable and thread-safe once built. Use [#builder(String)] to construct a configured
/// instance.
@NullMarked
public interface BusApi {
    /// Returns the current system time reported by the Bus Tracker API.
    ///
    /// @return the API system time as an [Instant]
    /// @throws Cta4jException if the API returns an error response or the response cannot be parsed
    Instant systemTime();

    /// Provides access to vehicle-related endpoints.
    ///
    /// @return the [VehiclesApi]
    VehiclesApi vehicles();

    /// Provides access to route-related endpoints.
    ///
    /// @return the [RoutesApi]
    RoutesApi routes();

    /// Provides access to direction-related endpoints.
    ///
    /// @return the [DirectionsApi]
    DirectionsApi directions();

    /// Provides access to stop-related endpoints.
    ///
    /// @return the [StopsApi]
    StopsApi stops();

    /// Provides access to route pattern–related endpoints.
    ///
    /// @return the [PatternsApi]
    PatternsApi patterns();

    /// Provides access to prediction-related endpoints.
    ///
    /// @return the [PredictionsApi]
    PredictionsApi predictions();

    /// Provides access to locale and language-related endpoints.
    ///
    /// @return the [LocalesApi]
    LocalesApi locales();

    /// Provides access to detour-related endpoints.
    ///
    /// @return the [DetoursApi]
    DetoursApi detours();

    /// Builder for constructing [BusApi] instances.
    interface Builder {
        /// Sets the API host to use for requests.
        ///
        /// If not specified, the default CTA Bus Tracker API host is used.
        ///
        /// @param host the API host
        /// @return this `Builder` instance
        /// @throws NullPointerException if `host` is `null`
        Builder host(String host);

        /// Builds a configured `BusApi` instance.
        ///
        /// @return a new `BusApi`
        BusApi build();
    }

    /// Creates a new `Builder` for constructing a `BusApi`.
    ///
    /// @param apiKey the CTA Bus Tracker API key
    /// @return a new `Builder`
    /// @throws NullPointerException if `apiKey` is `null`
    static Builder builder(String apiKey) {
        Objects.requireNonNull(apiKey);

        return new BusApiImpl.BuilderImpl(apiKey);
    }
}
