package com.cta4j.train;

import com.cta4j.train.arrival.ArrivalsApi;
import com.cta4j.train.common.internal.impl.TrainApiImpl;
import com.cta4j.train.follow.FollowApi;
import com.cta4j.train.location.LocationsApi;
import com.cta4j.train.station.StationsApi;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/// Primary entry point for interacting with the CTA Train Tracker API.
///
/// This interface provides grouped sub-APIs for different aspects of the Train Tracker API, such as stations,
/// arrivals, train following, and locations.
///
/// Instances of `TrainApi` are immutable and thread-safe once built. Use [#builder(String)] to construct a configured
/// instance.
@NullMarked
public interface TrainApi {
    /// Provides access to station-related endpoints.
    ///
    /// @return the [StationsApi]
    StationsApi stations();

    /// Provides access to arrival-related endpoints.
    ///
    /// @return the [ArrivalsApi]
    ArrivalsApi arrivals();

    /// Provides access to train follow-related endpoints.
    ///
    /// @return the [FollowApi]
    FollowApi follow();

    /// Provides access to location-related endpoints.
    ///
    /// @return the [LocationsApi]
    LocationsApi locations();

    /// Builder for constructing [TrainApi] instances.
    interface Builder {
        /// Sets the API host to use for requests.
        ///
        /// If not specified, the default CTA Train Tracker API host is used.
        ///
        /// @param host the API host
        /// @return this builder instance
        /// @throws NullPointerException if `host` is `null`
        Builder host(String host);

        /// Sets the URL to fetch station data from.
        ///
        /// If not specified, the default URL for station data is used.
        ///
        /// @param stationsUrl the URL for station data
        /// @return this builder instance
        /// @throws NullPointerException if `stationsUrl` is `null`
        Builder stationsUrl(String stationsUrl);

        /// Builds a configured [TrainApi] instance.
        ///
        /// @return a new [TrainApi]
        TrainApi build();
    }

    /// Creates a new [Builder] for constructing a [TrainApi].
    ///
    /// @param apiKey the CTA Train Tracker API key
    /// @return a new [Builder]
    /// @throws NullPointerException if `apiKey` is `null`
    static Builder builder(String apiKey) {
        Objects.requireNonNull(apiKey);

        return new TrainApiImpl.BuilderImpl(apiKey);
    }
}
