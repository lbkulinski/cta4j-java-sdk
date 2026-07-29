package com.cta4j.train.arrival.query;

import com.cta4j.train.common.model.TrainLine;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/// Represents a query for train arrivals at a specific stop.
///
/// @param stopId the ID of the stop to retrieve arrivals for
/// @param line the optional train line to filter arrivals by
/// @param maxResults the optional maximum number of arrivals to return
@NullMarked
public record StopArrivalsQuery(
    String stopId,
    @Nullable TrainLine line,
    @Nullable Integer maxResults
) {
    /// Constructs a `StopArrivalsQuery`.
    ///
    /// @param stopId the ID of the stop to retrieve arrivals for
    /// @param line the optional train line to filter arrivals by
    /// @param maxResults the optional maximum number of arrivals to return
    /// @throws NullPointerException if `stopId` is `null`
    /// @throws IllegalArgumentException if `maxResults` is non-`null` and not positive
    public StopArrivalsQuery {
        Objects.requireNonNull(stopId);

        if ((maxResults != null) && (maxResults <= 0)) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /// Creates a builder for `StopArrivalsQuery`.
    ///
    /// @param stopId the ID of the stop to retrieve arrivals for
    /// @return a new `Builder` instance
    /// @throws NullPointerException if `stopId` is `null`
    public static Builder builder(String stopId) {
        return new Builder(stopId);
    }

    /// A builder for `StopArrivalsQuery`.
    public static final class Builder {
        private final String stopId;

        @Nullable
        private TrainLine line;

        @Nullable
        private Integer maxResults;

        /// Constructs a `Builder`.
        ///
        /// @param stopId the ID of the stop to retrieve arrivals for
        /// @throws NullPointerException if `stopId` is `null`
        public Builder(String stopId) {
            this.stopId = Objects.requireNonNull(stopId);
        }

        /// Sets the train line to filter arrivals by.
        ///
        /// @param line the train line
        /// @return this `Builder` instance
        /// @throws NullPointerException if `line` is `null`
        public Builder line(TrainLine line) {
            this.line = Objects.requireNonNull(line);

            return this;
        }

        /// Sets the maximum number of arrivals to return.
        ///
        /// @param maxResults the maximum number of arrivals
        /// @return this `Builder` instance
        /// @throws IllegalArgumentException if `maxResults` is not positive
        public Builder maxResults(int maxResults) {
            if (maxResults <= 0) {
                throw new IllegalArgumentException("maxResults must be positive");
            }

            this.maxResults = maxResults;

            return this;
        }

        /// Builds the `StopArrivalsQuery`.
        ///
        /// @return a new `StopArrivalsQuery` instance
        public StopArrivalsQuery build() {
            return new StopArrivalsQuery(
                this.stopId,
                this.line,
                this.maxResults
            );
        }
    }
}
