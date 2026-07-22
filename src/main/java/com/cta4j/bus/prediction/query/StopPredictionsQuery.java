package com.cta4j.bus.prediction.query;

import com.cta4j.bus.common.internal.util.ApiUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a query for bus arrival predictions.
 *
 * @param stopIds the {@link List} of stop IDs to retrieve predictions for
 * @param routeIds the optional {@link List} of route IDs to filter predictions by
 * @param maxResults the optional maximum number of predictions to return
 */
@NullMarked
public record StopPredictionsQuery(
    List<String> stopIds,
    @Nullable List<String> routeIds,
    @Nullable Integer maxResults
) {
    /**
     * Constructs a {@code StopPredictionsQuery}.
     *
     * @param stopIds the {@link List} of stop IDs to retrieve predictions for
     * @param routeIds the optional {@link List} of route IDs to filter predictions by
     * @param maxResults the optional maximum number of predictions to return
     * @throws NullPointerException if {@code stopIds} is {@code null}, or if any element of {@code stopIds} or
     * {@code routeIds} is {@code null}
     * @throws IllegalArgumentException if more than 10 stop IDs are provided, or if {@code maxResults} is
     * non-{@code null} and not positive
     */
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

    /**
     * Creates a builder for {@code StopPredictionsQuery}.
     *
     * @param stopIds the {@link Collection} of stop IDs to retrieve predictions for
     * @return a new {@code Builder} instance
     * @throws NullPointerException if {@code stopIds} is {@code null}, or if any element of {@code stopIds} is
     * {@code null}
     */
    public static Builder builder(Collection<String> stopIds) {
        return new Builder(stopIds);
    }

    /**
     * A builder for {@code StopPredictionsQuery}.
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
         * @param stopIds the {@link Collection} of stop IDs to retrieve predictions for
         * @throws NullPointerException if {@code stopIds} is {@code null}, or if any element of {@code stopIds} is
         * {@code null}
         */
        public Builder(Collection<String> stopIds) {
            Objects.requireNonNull(stopIds);

            this.stopIds = List.copyOf(stopIds);
        }

        /**
         * Sets the {@link Collection} of route IDs to filter predictions by.
         *
         * @param routeIds the {@link Collection} of route IDs
         * @return this {@code Builder} instance
         * @throws NullPointerException if {@code routeIds} is {@code null}, or if any element of {@code routeIds} is
         * {@code null}
         */
        public Builder routeIds(Collection<String> routeIds) {
            Objects.requireNonNull(routeIds);

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
         * Builds the {@code StopPredictionsQuery}.
         *
         * @return a new {@code StopPredictionsQuery} instance
         * @throws IllegalArgumentException if more than 10 stop IDs are provided
         */
        public StopPredictionsQuery build() {
            return new StopPredictionsQuery(
                this.stopIds,
                this.routeIds,
                this.maxResults
            );
        }
    }
}
