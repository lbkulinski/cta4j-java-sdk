package com.cta4j.bus.prediction.query;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Represents a query for bus arrival predictions.
 *
 * @param stopIds the {@code List} of stop IDs to retrieve predictions for
 * @param routeIds the optional {@code List} of route IDs to filter predictions by
 * @param maxResults the optional maximum number of predictions to return
 */
@NullMarked
public record StopsPredictionsQuery(
    List<String> stopIds,

    @Nullable
    List<String> routeIds,

    @Nullable
    Integer maxResults
) {
    /**
     * Constructs a {@code StopsPredictionsQuery}.
     *
     * @param stopIds the {@code List} of stop IDs to retrieve predictions for
     * @param routeIds the optional {@code List} of route IDs to filter predictions by
     * @param maxResults the optional maximum number of predictions to return
     * @throws NullPointerException if {@code stopIds} or any of its elements are {@code null}, or if any element of
     * {@code routeIds} is {@code null}
     * @throws IllegalArgumentException if {@code maxResults} is non-{@code null} and not positive
     */
    public StopsPredictionsQuery {
        Objects.requireNonNull(stopIds);

        stopIds.forEach(Objects::requireNonNull);

        stopIds = List.copyOf(stopIds);

        if (routeIds != null) {
            routeIds.forEach(Objects::requireNonNull);

            routeIds = List.copyOf(routeIds);
        }

        if ((maxResults != null) && (maxResults <= 0)) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /**
     * Creates a builder for {@code StopsPredictionsQuery}.
     *
     * @param stopIds the {@code List} of stop IDs to retrieve predictions for
     * @return a new {@code Builder} instance
     * @throws NullPointerException if {@code stopIds} or any of its elements are {@code null}
     */
    public static Builder builder(List<String> stopIds) {
        Objects.requireNonNull(stopIds);

        stopIds.forEach(Objects::requireNonNull);

        stopIds = List.copyOf(stopIds);

        return new Builder(stopIds);
    }

    /**
     * A builder for {@code StopsPredictionsQuery}.
     */
    public static final class Builder {
        /**
         * The {@code List} of stop IDs to retrieve predictions for.
         */
        private final List<String> stopIds;

        /**
         * The optional {@code List} of route IDs to filter predictions by.
         */
        @Nullable
        private List<String> routeIds;

        /**
         * The optional maximum number of predictions to return.
         */
        @Nullable
        private Integer maxResults;

        /**
         * Constructs a {@code Builder}.
         *
         * @param stopIds the {@code List} of stop IDs to retrieve predictions for
         * @throws NullPointerException if {@code stopIds} or any of its elements are {@code null}
         */
        public Builder(List<String> stopIds) {
            Objects.requireNonNull(stopIds);

            stopIds.forEach(Objects::requireNonNull);

            this.stopIds = List.copyOf(stopIds);
        }

        /**
         * Sets the {@code List} of route IDs to filter predictions by.
         *
         * @param routeIds the {@code List} of route IDs
         * @return this {@code Builder} instance
         * @throws NullPointerException if {@code routeIds} or any of its elements are {@code null}
         */
        public Builder routeIds(List<String> routeIds) {
            Objects.requireNonNull(routeIds);

            routeIds.forEach(Objects::requireNonNull);

            this.routeIds = List.copyOf(routeIds);

            return this;
        }

        /**
         * Sets the maximum number of predictions to return.
         *
         * @param maxResults the maximum number of predictions
         * @return this {@code Builder} instance
         * @throws NullPointerException if {@code maxResults} is {@code null}
         * @throws IllegalArgumentException if {@code maxResults} is not positive
         */
        public Builder maxResults(Integer maxResults) {
            Objects.requireNonNull(maxResults);

            if (maxResults <= 0) {
                throw new IllegalArgumentException("maxResults must be positive");
            }

            this.maxResults = maxResults;

            return this;
        }

        /**
         * Builds the {@code StopsPredictionsQuery}.
         *
         * @return a new {@code StopsPredictionsQuery} instance
         */
        public StopsPredictionsQuery build() {
            return new StopsPredictionsQuery(
                this.stopIds,
                this.routeIds,
                this.maxResults
            );
        }
    }
}
