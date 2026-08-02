package com.cta4j.train.arrival.query;

import com.cta4j.train.common.internal.util.TrainApiUtils;
import com.cta4j.train.common.model.TrainLine;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/// Represents a query for train arrivals at a specific map.
///
/// @param mapIds the [List] of map IDs to retrieve arrivals for
/// @param line the optional train line to filter arrivals by
/// @param maxResults the optional maximum number of arrivals to return
@NullMarked
public record MapArrivalsQuery(
    List<String> mapIds,
    @Nullable TrainLine line,
    @Nullable Integer maxResults
) {
    /// Constructs a `MapArrivalsQuery`.
    ///
    /// @param mapIds the [List] of map IDs to retrieve arrivals for
    /// @param line the optional train line to filter arrivals by
    /// @param maxResults the optional maximum number of arrivals to return
    /// @throws NullPointerException if `mapIds` is `null`, or if any element of `mapIds` is `null`
    /// @throws IllegalArgumentException if more than 4 map IDs are provided, or if `maxResults` is non-`null` and not
    /// positive
    public MapArrivalsQuery {
        Objects.requireNonNull(mapIds);

        mapIds = List.copyOf(mapIds);

        TrainApiUtils.requireMaxIds(mapIds, "map");

        if ((maxResults != null) && (maxResults <= 0)) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /// Creates a new `Builder` for constructing a `MapArrivalsQuery`.
    ///
    /// @param mapIds the [Collection] of map IDs to retrieve arrivals for
    /// @return a new `Builder`
    /// @throws NullPointerException if `mapIds` is `null`, or if any element of `mapIds` is `null`
    public static Builder builder(Collection<String> mapIds) {
        return new Builder(mapIds);
    }

    /// A builder for `MapArrivalsQuery`.
    public static final class Builder {
        private final List<String> mapIds;

        @Nullable
        private TrainLine line;

        @Nullable
        private Integer maxResults;

        private Builder(Collection<String> mapIds) {
            Objects.requireNonNull(mapIds);

            this.mapIds = List.copyOf(mapIds);
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
        /// @throws IllegalArgumentException if more than 4 map IDs are provided
        public MapArrivalsQuery build() {
            return new MapArrivalsQuery(
                this.mapIds,
                this.line,
                this.maxResults
            );
        }
    }
}
