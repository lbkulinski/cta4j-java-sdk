package com.cta4j.train;

import com.cta4j.train.internal.impl.TrainApiImpl;
import com.cta4j.train.station.StationsApi;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public interface TrainApi {
    StationsApi stations();

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
