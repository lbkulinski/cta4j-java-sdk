package com.cta4j.bus.prediction.query;

import com.cta4j.bus.common.internal.util.ApiUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/// Represents a query for bus arrival predictions.
///
/// @param stopIds the [List] of stop IDs to retrieve predictions for
/// @param routeIds the optional [List] of route IDs to filter predictions by
/// @param maxResults the optional maximum number of predictions to return
@NullMarked
public record StopPredictionsQuery(
    List<String> stopIds,
    @Nullable List<String> routeIds,
    @Nullable Integer maxResults
) {
    /// Constructs a `StopPredictionsQuery`.
    ///
    /// @param stopIds the [List] of stop IDs to retrieve predictions for
    /// @param routeIds the optional [List] of route IDs to filter predictions by
    /// @param maxResults the optional maximum number of predictions to return
    /// @throws NullPointerException if `stopIds` is `null`, or if any element of `stopIds` or `routeIds` is `null`
    /// @throws IllegalArgumentException if more than 10 stop IDs are provided, or if `maxResults` is non-`null` and
    /// not positive
    public StopPredictionsQuery {
        Objects.requireNonNull(stopIds);

        ApiUtils.requireMaxIds(stopIds, "stop");

        stopIds = List.copyOf(stopIds);

        if (routeIds != null) {
            routeIds = List.copyOf(routeIds);
        }

        if (maxResults != null && maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /// Creates a new `Builder` for constructing a `StopPredictionsQuery`.
    ///
    /// @param stopIds the [Collection] of stop IDs to retrieve predictions for
    /// @return a new `Builder`
    /// @throws NullPointerException if `stopIds` is `null`, or if any element of `stopIds` is `null`
    public static Builder builder(Collection<String> stopIds) {
        return new Builder(stopIds);
    }

    /// A builder for `StopPredictionsQuery`.
    public static final class Builder {
        private final List<String> stopIds;

        @Nullable
        private List<String> routeIds;

        @Nullable
        private Integer maxResults;

        /// Constructs a `Builder`.
        ///
        /// @param stopIds the [Collection] of stop IDs to retrieve predictions for
        /// @throws NullPointerException if `stopIds` is `null`, or if any element of `stopIds` is `null`
        public Builder(Collection<String> stopIds) {
            Objects.requireNonNull(stopIds);

            this.stopIds = List.copyOf(stopIds);
        }

        /// Sets the [Collection] of route IDs to filter predictions by.
        ///
        /// @param routeIds the [Collection] of route IDs
        /// @return this `Builder` instance
        /// @throws NullPointerException if `routeIds` is `null`, or if any element of `routeIds` is `null`
        public Builder routeIds(Collection<String> routeIds) {
            Objects.requireNonNull(routeIds);

            this.routeIds = List.copyOf(routeIds);

            return this;
        }

        /// Sets the maximum number of predictions to return.
        ///
        /// @param maxResults the maximum number of predictions
        /// @return this `Builder` instance
        /// @throws IllegalArgumentException if `maxResults` is not positive
        public Builder maxResults(int maxResults) {
            if (maxResults <= 0) {
                throw new IllegalArgumentException("maxResults must be positive");
            }

            this.maxResults = maxResults;

            return this;
        }

        /// Builds a configured `StopPredictionsQuery` instance.
        ///
        /// @return a new `StopPredictionsQuery`
        /// @throws IllegalArgumentException if more than 10 stop IDs are provided
        public StopPredictionsQuery build() {
            return new StopPredictionsQuery(
                this.stopIds,
                this.routeIds,
                this.maxResults
            );
        }
    }
}
