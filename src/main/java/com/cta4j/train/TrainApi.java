package com.cta4j.train;

import com.cta4j.train.arrival.ArrivalsApi;
import com.cta4j.train.follow.FollowApi;
import com.cta4j.train.common.internal.impl.TrainApiImpl;
import com.cta4j.train.location.LocationsApi;
import com.cta4j.train.station.StationsApi;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Primary entry point for interacting with the CTA Train Tracker API.
 * <p>
 * This interface provides grouped sub-APIs for different aspects of the Train Tracker API, such as stations, arrivals,
 * train following, and locations.
 * <p>
 * Instances of {@code TrainApi} are immutable and thread-safe once built.
 * Use {@link #builder(String)} to construct a configured instance.
 */
@NullMarked
public interface TrainApi {
    /**
     * Provides access to station-related endpoints.
     *
     * @return the {@link StationsApi}
     */
    StationsApi stations();

    /**
     * Provides access to arrival-related endpoints.
     *
     * @return the {@link ArrivalsApi}
     */
    ArrivalsApi arrivals();

    /**
     * Provides access to train follow-related endpoints.
     *
     * @return the {@link FollowApi}
     */
    FollowApi follow();

    /**
     * Provides access to location-related endpoints.
     *
     * @return the {@link LocationsApi}
     */
    LocationsApi locations();

    /**
     * Builder for constructing {@link TrainApi} instances.
     */
    interface Builder {
        /**
         * Sets the API host to use for requests.
         * <p>
         * If not specified, the default CTA Train Tracker API host is used.
         *
         * @param host the API host
         * @return this builder instance
         */
        Builder host(String host);

        /**
         * Sets the URL to fetch station data from.
         * <p>
         * If not specified, the default URL for station data is used.
         *
         * @param stationsUrl the URL for station data
         * @return this builder instance
         */
        Builder stationsUrl(String stationsUrl);

        /**
         * Builds a configured {@link TrainApi} instance.
         *
         * @return a new {@link TrainApi}
         */
        TrainApi build();
    }

    /**
     * Creates a new {@link Builder} for constructing a {@link TrainApi}.
     *
     * @param apiKey the CTA Train Tracker API key
     * @return a new {@link Builder}
     * @throws NullPointerException if {@code apiKey} is {@code null}
     */
    static Builder builder(String apiKey) {
        Objects.requireNonNull(apiKey);

        return new TrainApiImpl.BuilderImpl(apiKey);
    }
}
