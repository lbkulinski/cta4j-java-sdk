package com.cta4j.bus.prediction.query;

import com.cta4j.bus.common.internal.util.ApiUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a query for vehicle arrival predictions.
 *
 * @param vehicleIds the {@link List} of vehicle IDs to retrieve predictions for
 * @param maxResults the optional maximum number of predictions to return
 */
@NullMarked
public record VehiclePredictionsQuery(
    List<String> vehicleIds,
    @Nullable Integer maxResults
) {
    /**
     * Constructs a {@code VehiclePredictionsQuery}.
     *
     * @param vehicleIds the {@link List} of vehicle IDs to retrieve predictions for
     * @param maxResults the optional maximum number of predictions to return
     * @throws NullPointerException if {@code vehicleIds} is {@code null}, or if any element of {@code vehicleIds} is
     * {@code null}
     * @throws IllegalArgumentException if more than 10 vehicle IDs are provided, or if {@code maxResults} is
     * non-{@code null} and not positive
     */
    public VehiclePredictionsQuery {
        Objects.requireNonNull(vehicleIds);

        ApiUtils.requireMaxIds(vehicleIds, "vehicle");

        vehicleIds = List.copyOf(vehicleIds);

        if (maxResults != null && maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /**
     * Creates a builder for {@code VehiclePredictionsQuery}.
     *
     * @param vehicleIds the {@link Collection} of vehicle IDs to retrieve predictions for
     * @return a new {@code Builder} instance
     * @throws NullPointerException if {@code vehicleIds} is {@code null}, or if any element of {@code vehicleIds} is
     * {@code null}
     */
    public static Builder builder(Collection<String> vehicleIds) {
        return new Builder(vehicleIds);
    }

    /**
     * Builder for {@code VehiclePredictionsQuery}.
     */
    public static final class Builder {
        /**
         * The {@link List} of vehicle IDs to retrieve predictions for.
         */
        private final List<String> vehicleIds;

        /**
         * The optional maximum number of predictions to return.
         */
        @Nullable
        private Integer maxResults;

        /**
         * Constructs a {@code Builder}.
         *
         * @param vehicleIds the {@link Collection} of vehicle IDs to retrieve predictions for
         * @throws NullPointerException if {@code vehicleIds} is {@code null}, or if any element of
         * {@code vehicleIds} is {@code null}
         */
        public Builder(Collection<String> vehicleIds) {
            Objects.requireNonNull(vehicleIds);

            this.vehicleIds = List.copyOf(vehicleIds);
        }

        /**
         * Sets the maximum number of predictions to return.
         *
         * @param maxResults the maximum number of predictions to return
         * @return this {@code Builder} instance
         * @throws IllegalArgumentException if {@code maxResults} is not positive
         */
        public Builder maxResults(int maxResults) {
            if (maxResults <= 0) {
                throw new IllegalArgumentException("maxResults must be positive");
            }

            this.maxResults = maxResults;

            return this;
        }

        /**
         * Builds the {@code VehiclePredictionsQuery}.
         *
         * @return the constructed {@code VehiclePredictionsQuery}
         * @throws IllegalArgumentException if more than 10 vehicle IDs are provided
         */
        public VehiclePredictionsQuery build() {
            return new VehiclePredictionsQuery(
                this.vehicleIds,
                this.maxResults
            );
        }
    }
}
