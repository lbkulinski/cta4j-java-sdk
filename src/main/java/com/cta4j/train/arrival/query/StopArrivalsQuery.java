package com.cta4j.train.arrival.query;

import com.cta4j.train.common.internal.util.TrainApiUtils;
import com.cta4j.train.common.model.TrainLine;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/// Represents a query for train arrivals at a specific stop.
///
/// @param stopIds the [List] of stop IDs to retrieve arrivals for
/// @param line the optional train line to filter arrivals by
/// @param maxResults the optional maximum number of arrivals to return
@NullMarked
public record StopArrivalsQuery(
    List<String> stopIds,
    @Nullable TrainLine line,
    @Nullable Integer maxResults
) {
    /// Constructs a `StopArrivalsQuery`.
    ///
    /// @param stopIds the [List] of stop IDs to retrieve arrivals for
    /// @param line the optional train line to filter arrivals by
    /// @param maxResults the optional maximum number of arrivals to return
    /// @throws NullPointerException if `stopIds` is `null`, or if any element of `stopIds` is `null`
    /// @throws IllegalArgumentException if more than 4 stop IDs are provided, or if `maxResults` is non-`null` and not
    /// positive
    public StopArrivalsQuery {
        Objects.requireNonNull(stopIds);

        stopIds = List.copyOf(stopIds);

        TrainApiUtils.requireMaxIds(stopIds, "stop");

        if ((maxResults != null) && (maxResults <= 0)) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /// Creates a new `Builder` for constructing a `StopArrivalsQuery`.
    ///
    /// @param stopIds the [Collection] of stop IDs to retrieve arrivals for
    /// @return a new `Builder`
    /// @throws NullPointerException if `stopIds` is `null`, or if any element of `stopIds` is `null`
    public static Builder builder(Collection<String> stopIds) {
        return new Builder(stopIds);
    }

    /// A builder for `StopArrivalsQuery`.
    public static final class Builder {
        private final List<String> stopIds;

        @Nullable
        private TrainLine line;

        @Nullable
        private Integer maxResults;

        private Builder(Collection<String> stopIds) {
            Objects.requireNonNull(stopIds);

            this.stopIds = List.copyOf(stopIds);
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

        /// Builds a configured `StopArrivalsQuery` instance.
        ///
        /// @return a new `StopArrivalsQuery`
        /// @throws IllegalArgumentException if more than 4 stop IDs are provided
        public StopArrivalsQuery build() {
            return new StopArrivalsQuery(
                this.stopIds,
                this.line,
                this.maxResults
            );
        }
    }
}
