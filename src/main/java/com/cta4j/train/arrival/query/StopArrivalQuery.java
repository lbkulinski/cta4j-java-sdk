package com.cta4j.train.arrival.query;

import com.cta4j.train.common.model.TrainLine;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a query for train arrival results for a specific stop.
 *
 * @param stopId the ID of the stop to retrieve arrival results for
 * @param line the optional train line to filter arrival results by
 * @param maxResults the optional maximum number of arrival results to return
 */
@NullMarked
public record StopArrivalQuery(
    String stopId,

    @Nullable
    TrainLine line,

    @Nullable
    Integer maxResults
) {
    /**
     * Constructs a {@code StopArrivalQuery}.
     *
     * @param stopId the ID of the stop to retrieve arrival results for
     * @param line the optional train line to filter arrival results by
     * @param maxResults the optional maximum number of arrival results to return
     * @throws NullPointerException if {@code stopId} is {@code null}
     * @throws IllegalArgumentException if {@code maxResults} is non-{@code null} and not positive
     */
    public StopArrivalQuery {
        Objects.requireNonNull(stopId);

        if ((maxResults != null) && (maxResults <= 0)) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /**
     * A builder for {@code StopArrivalQuery}.
     */
    public static final class Builder {
        /**
         * The ID of the stop to retrieve arrival results for.
         */
        private final String stopId;

        /**
         * The optional train line to filter arrival results by.
         */
        @Nullable
        private TrainLine line;

        /**
         * The optional maximum number of arrival results to return.
         */
        @Nullable
        private Integer maxResults;

        /**
         * Constructs a {@code Builder}.
         *
         * @param stopId the ID of the stop to retrieve arrival results for
         * @throws NullPointerException if {@code stopId} is {@code null}
         */
        public Builder(String stopId) {
            this.stopId = Objects.requireNonNull(stopId);
        }

        /**
         * Sets the train line to filter arrival results by.
         *
         * @param line the train line
         * @return this {@code Builder} instance
         * @throws NullPointerException if {@code line} is {@code null}
         */
        public Builder line(TrainLine line) {
            this.line = line;

            return this;
        }

        /**
         * Sets the maximum number of arrival results to return.
         *
         * @param maxResults the maximum number of arrival results
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
         * Builds the {@code StopArrivalQuery}.
         *
         * @return a new {@code StopArrivalQuery} instance
         */
        public StopArrivalQuery build() {
            return new StopArrivalQuery(
                this.stopId,
                this.line,
                this.maxResults
            );
        }
    }
}
