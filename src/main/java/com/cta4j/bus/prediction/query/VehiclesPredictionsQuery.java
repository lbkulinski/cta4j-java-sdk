package com.cta4j.bus.prediction.query;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
public record VehiclesPredictionsQuery(
    List<String> vehicleIds,

    @Nullable
    Integer maxResults
) {
    public VehiclesPredictionsQuery {
        Objects.requireNonNull(vehicleIds);

        vehicleIds.forEach(Objects::requireNonNull);

        vehicleIds = List.copyOf(vehicleIds);

        if ((maxResults != null) && (maxResults <= 0)) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    public static Builder builder(List<String> stopIds) {
        Objects.requireNonNull(stopIds);

        stopIds.forEach(Objects::requireNonNull);

        return new Builder(stopIds);
    }

    public static final class Builder {
        private final List<String> vehicleIds;

        @Nullable
        private Integer maxResults;

        public Builder(List<String> vehicleIds) {
            Objects.requireNonNull(vehicleIds);

            vehicleIds.forEach(Objects::requireNonNull);

            this.vehicleIds = List.copyOf(vehicleIds);
        }

        public Builder maxResults(Integer maxResults) {
            Objects.requireNonNull(maxResults);

            if (maxResults <= 0) {
                throw new IllegalArgumentException("maxResults must be positive");
            }

            this.maxResults = maxResults;

            return this;
        }

        public VehiclesPredictionsQuery build() {
            return new VehiclesPredictionsQuery(
                this.vehicleIds,
                this.maxResults
            );
        }
    }
}
