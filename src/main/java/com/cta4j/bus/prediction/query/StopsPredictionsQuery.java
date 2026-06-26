package com.cta4j.bus.prediction.query;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Represents a query for bus arrival predictions.
 *
 * @param stopIds the {@link List} of stop IDs to retrieve predictions for
 * @param routeIds the optional {@link List} of route IDs to filter predictions by
 * @param maxResults the optional maximum number of predictions to return
 */
@NullMarked
public record StopsPredictionsQuery(
    List<String> stopIds,
    @Nullable List<String> routeIds,
    @Nullable Integer maxResults
) {
    private static final int MAX_STOP_IDS = 10;

    /**
     * Constructs a {@code StopsPredictionsQuery}.
     *
     * @param stopIds the {@link List} of stop IDs to retrieve predictions for
     * @param routeIds the optional {@link List} of route IDs to filter predictions by
     * @param maxResults the optional maximum number of predictions to return
     * @throws NullPointerException if {@code stopIds} or any of its elements are {@code null}, or if any element of
     * {@code routeIds} is {@code null}
     * @throws IllegalArgumentException if more than 10 stop IDs are provided, or if {@code maxResults} is
     * non-{@code null} and not positive
     */
    public StopsPredictionsQuery {
        stopIds = List.copyOf(stopIds);

        if (stopIds.size() > MAX_STOP_IDS) {
            String message = "A maximum of %d stop IDs can be requested at once, but %d were provided".formatted(
                MAX_STOP_IDS,
                stopIds.size()
            );

            throw new IllegalArgumentException(message);
        }

        if (routeIds != null) {
            routeIds = List.copyOf(routeIds);
        }

        if (maxResults != null && maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /**
     * Creates a builder for {@code StopsPredictionsQuery}.
     *
     * @param stopIds the {@link List} of stop IDs to retrieve predictions for
     * @return a new {@code Builder} instance
     * @throws NullPointerException if {@code stopIds} or any of its elements are {@code null}
     */
    public static Builder builder(List<String> stopIds) {
        return new Builder(stopIds);
    }

    /**
     * A builder for {@code StopsPredictionsQuery}.
     */
    public static final class Builder {
        /**
         * The {@link List} of stop IDs to retrieve predictions for.
         */
        private final List<String> stopIds;

        /**
         * The optional {@link List} of route IDs to filter predictions by.
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
         * @param stopIds the {@link List} of stop IDs to retrieve predictions for
         * @throws NullPointerException if {@code stopIds} or any of its elements are {@code null}
         */
        public Builder(List<String> stopIds) {
            this.stopIds = List.copyOf(stopIds);
        }

        /**
         * Sets the {@link List} of route IDs to filter predictions by.
         *
         * @param routeIds the {@link List} of route IDs
         * @return this {@code Builder} instance
         * @throws NullPointerException if {@code routeIds} or any of its elements are {@code null}
         */
        public Builder routeIds(List<String> routeIds) {
            this.routeIds = List.copyOf(routeIds);

            return this;
        }

        /**
         * Sets the maximum number of predictions to return.
         *
         * @param maxResults the maximum number of predictions
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
         * Builds the {@code StopsPredictionsQuery}.
         *
         * @return a new {@code StopsPredictionsQuery} instance
         * @throws IllegalArgumentException if more than 10 stop IDs are provided
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
