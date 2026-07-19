package com.cta4j.train.arrival.query;

import com.cta4j.common.train.TrainLine;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a query for train arrivals at a specific map.
 *
 * @param mapId the ID of the map to retrieve arrivals for
 * @param line the optional train line to filter arrivals by
 * @param maxResults the optional maximum number of arrivals to return
 */
@NullMarked
public record MapArrivalQuery(
    String mapId,
    @Nullable TrainLine line,
    @Nullable Integer maxResults
) {
    /**
     * Constructs a {@code MapArrivalQuery}.
     *
     * @param mapId the ID of the map to retrieve arrivals for
     * @param line the optional train line to filter arrivals by
     * @param maxResults the optional maximum number of arrivals to return
     * @throws NullPointerException if {@code mapId} is {@code null}
     * @throws IllegalArgumentException if {@code maxResults} is non-{@code null} and not positive
     */
    public MapArrivalQuery {
        Objects.requireNonNull(mapId);

        if ((maxResults != null) && (maxResults <= 0)) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
    }

    /**
     * Creates a builder for {@code MapArrivalQuery}.
     *
     * @param mapId the ID of the map to retrieve arrivals for
     * @return a new {@code Builder} instance
     * @throws NullPointerException if {@code mapId} is {@code null}
     */
    public static Builder builder(String mapId) {
        return new Builder(mapId);
    }

    /**
     * A builder for {@code MapArrivalQuery}.
     */
    public static final class Builder {
        /**
         * The ID of the map to retrieve arrivals for.
         */
        private final String mapId;

        /**
         * The optional train line to filter arrivals by.
         */
        @Nullable
        private TrainLine line;

        /**
         * The optional maximum number of arrivals to return.
         */
        @Nullable
        private Integer maxResults;

        /**
         * Constructs a {@code Builder}.
         *
         * @param mapId the ID of the map to retrieve arrivals for
         * @throws NullPointerException if {@code mapId} is {@code null}
         */
        public Builder(String mapId) {
            this.mapId = Objects.requireNonNull(mapId);
        }

        /**
         * Sets the train line to filter arrivals by.
         *
         * @param line the train line
         * @return this {@code Builder} instance
         * @throws NullPointerException if {@code line} is {@code null}
         */
        public Builder line(TrainLine line) {
            this.line = Objects.requireNonNull(line);

            return this;
        }

        /**
         * Sets the maximum number of arrivals to return.
         *
         * @param maxResults the maximum number of arrivals
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
         * Builds the {@code MapArrivalQuery}.
         *
         * @return a new {@code MapArrivalQuery} instance
         */
        public MapArrivalQuery build() {
            return new MapArrivalQuery(
                this.mapId,
                this.line,
                this.maxResults
            );
        }
    }
}
