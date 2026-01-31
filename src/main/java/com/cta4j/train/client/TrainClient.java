package com.cta4j.train.client;

import com.cta4j.train.client.internal.TrainClientImpl;
import com.cta4j.exception.Cta4jException;
import com.cta4j.train.model.StationArrival;
import com.cta4j.train.model.Train;

import java.util.List;
import java.util.Optional;

/**
 * A client for interacting with the CTA Train Tracker API.
 */
public interface TrainClient {
    /**
     * Retrieves a {@link List} of upcoming arrivals for a specific station.
     *
     * @param stationId the ID of the station
     * @return a {@link List} of upcoming arrivals for the specified station
     * @throws NullPointerException if the specified station ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    List<StationArrival> getStationArrivals(String stationId);

    /**
     * Retrieves information about a specific train by its run number.
     *
     * @param run the run number of the train
     * @return an {@link Optional} containing the train information if found, or an empty {@link Optional} if not found
     * @throws NullPointerException if the specified run number is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    Optional<Train> getTrain(String run);

    /**
     * A builder for configuring and creating {@link TrainClient} instances.
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
         * Builds a configured {@link TrainClient} instance.
         *
         * @return a new {@link TrainClient}
         */
        TrainClient build();
    }

    /**
     * Creates a new {@link Builder} for configuring and building a {@link TrainClient}.
     *
     * @return a new {@link Builder} instance
     */
    static Builder builder() {
        return new TrainClientImpl.BuilderImpl();
    }
}
