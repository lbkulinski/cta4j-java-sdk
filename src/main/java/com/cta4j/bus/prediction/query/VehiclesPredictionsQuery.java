package com.cta4j.bus.prediction.query;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Represents a query for vehicle arrival predictions.
 *
 * @param vehicleIds the {@link List} of vehicle IDs to retrieve predictions for
 * @param maxResults the optional maximum number of predictions to return
 */
@NullMarked
public record VehiclesPredictionsQuery(
    List<String> vehicleIds,
    @Nullable Integer maxResults
) {
    private static final int MAX_VEHICLE_IDS = 10;

    /**
     * Constructs a {@code VehiclesPredictionsQuery}.
     *
     * @param vehicleIds the {@link List} of vehicle IDs to retrieve predictions for
     * @param maxResults the optional maximum number of predictions to return
     * @throws NullPointerException if {@code vehicleIds} or any of its elements are {@code null}
     * @throws IllegalArgumentException if {@code maxResults} is non-{@code null} and not positive
     */
    public VehiclesPredictionsQuery {
        vehicleIds = List.copyOf(vehicleIds);

        if (vehicleIds.size() > MAX_VEHICLE_IDS) {
            String message = "A maximum of %d vehicle IDs can be requested at once, but %d were provided".formatted(
                MAX_VEHICLE_IDS,
                vehicleIds.size()
            );

            throw new IllegalArgumentException(message);
        }

        if (maxResults != null && maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /**
     * Creates a builder for {@code VehiclesPredictionsQuery}.
     *
     * @param vehicleIds the {@link List} of vehicle IDs to retrieve predictions for
     * @return a new {@code Builder} instance
     * @throws NullPointerException if {@code vehicleIds} or any of its elements are {@code null}
     */
    public static Builder builder(List<String> vehicleIds) {
        return new Builder(vehicleIds);
    }

    /**
     * Builder for {@code VehiclesPredictionsQuery}.
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
         * @param vehicleIds the {@link List} of vehicle IDs to retrieve predictions for
         * @throws NullPointerException if {@code vehicleIds} or any of its elements are {@code null}
         */
        public Builder(List<String> vehicleIds) {
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
         * Builds the {@code VehiclesPredictionsQuery}.
         *
         * @return the constructed {@code VehiclesPredictionsQuery}
         */
        public VehiclesPredictionsQuery build() {
            return new VehiclesPredictionsQuery(
                this.vehicleIds,
                this.maxResults
            );
        }
    }
}
