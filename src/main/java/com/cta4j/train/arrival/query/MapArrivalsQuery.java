package com.cta4j.train.arrival.query;

import com.cta4j.train.common.model.TrainLine;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/// Represents a query for train arrivals at a specific map.
///
/// @param mapId the ID of the map to retrieve arrivals for
/// @param line the optional train line to filter arrivals by
/// @param maxResults the optional maximum number of arrivals to return
@NullMarked
public record MapArrivalsQuery(
    String mapId,
    @Nullable TrainLine line,
    @Nullable Integer maxResults
) {
    /// Constructs a `MapArrivalsQuery`.
    ///
    /// @param mapId the ID of the map to retrieve arrivals for
    /// @param line the optional train line to filter arrivals by
    /// @param maxResults the optional maximum number of arrivals to return
    /// @throws NullPointerException if `mapId` is `null`
    /// @throws IllegalArgumentException if `maxResults` is non-`null` and not positive
    public MapArrivalsQuery {
        Objects.requireNonNull(mapId);

        if ((maxResults != null) && (maxResults <= 0)) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /// Creates a new `Builder` for constructing a `MapArrivalsQuery`.
    ///
    /// @param mapId the ID of the map to retrieve arrivals for
    /// @return a new `Builder`
    /// @throws NullPointerException if `mapId` is `null`
    public static Builder builder(String mapId) {
        return new Builder(mapId);
    }

    /// A builder for `MapArrivalsQuery`.
    public static final class Builder {
        private final String mapId;

        @Nullable
        private TrainLine line;

        @Nullable
        private Integer maxResults;

        /// Constructs a `Builder`.
        ///
        /// @param mapId the ID of the map to retrieve arrivals for
        /// @throws NullPointerException if `mapId` is `null`
        public Builder(String mapId) {
            this.mapId = Objects.requireNonNull(mapId);
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

        /// Builds a configured `MapArrivalsQuery` instance.
        ///
        /// @return a new `MapArrivalsQuery`
        public MapArrivalsQuery build() {
            return new MapArrivalsQuery(
                this.mapId,
                this.line,
                this.maxResults
            );
        }
    }
}
