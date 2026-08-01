package com.cta4j.bus.prediction.query;

import com.cta4j.bus.common.internal.util.BusApiUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/// Represents a query for vehicle arrival predictions.
///
/// @param vehicleIds the [List] of vehicle IDs to retrieve predictions for
/// @param maxResults the optional maximum number of predictions to return
@NullMarked
public record VehiclePredictionsQuery(
    List<String> vehicleIds,
    @Nullable Integer maxResults
) {
    /// Constructs a `VehiclePredictionsQuery`.
    ///
    /// @param vehicleIds the [List] of vehicle IDs to retrieve predictions for
    /// @param maxResults the optional maximum number of predictions to return
    /// @throws NullPointerException if `vehicleIds` is `null`, or if any element of `vehicleIds` is `null`
    /// @throws IllegalArgumentException if more than 10 vehicle IDs are provided, or if `maxResults` is non-`null` and
    /// not positive
    public VehiclePredictionsQuery {
        Objects.requireNonNull(vehicleIds);

        BusApiUtils.requireMaxIds(vehicleIds, "vehicle");

        vehicleIds = List.copyOf(vehicleIds);

        if (maxResults != null && maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /// Creates a new `Builder` for constructing a `VehiclePredictionsQuery`.
    ///
    /// @param vehicleIds the [Collection] of vehicle IDs to retrieve predictions for
    /// @return a new `Builder`
    /// @throws NullPointerException if `vehicleIds` is `null`, or if any element of `vehicleIds` is `null`
    public static Builder builder(Collection<String> vehicleIds) {
        return new Builder(vehicleIds);
    }

    /// A builder for `VehiclePredictionsQuery`.
    public static final class Builder {
        private final List<String> vehicleIds;

        @Nullable
        private Integer maxResults;

        private Builder(Collection<String> vehicleIds) {
            Objects.requireNonNull(vehicleIds);

            this.vehicleIds = List.copyOf(vehicleIds);
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

        /// Builds a configured `VehiclePredictionsQuery` instance.
        ///
        /// @return a new `VehiclePredictionsQuery`
        /// @throws IllegalArgumentException if more than 10 vehicle IDs are provided
        public VehiclePredictionsQuery build() {
            return new VehiclePredictionsQuery(
                this.vehicleIds,
                this.maxResults
            );
        }
    }
}
