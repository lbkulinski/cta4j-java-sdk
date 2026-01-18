package com.cta4j.bus.api.prediction.query;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NullMarked
public record StopsPredictionsQuery(
    Collection<String> stopIds,

    @Nullable
    Collection<String> routeIds,

    @Nullable
    Integer maxResults
) {
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

    public static Builder builder(Collection<String> stopIds) {
        return new Builder(stopIds);
    }

    public static final class Builder {
        private final Collection<String> stopIds;

        @Nullable
        private Collection<String> routeIds;

        @Nullable
        private Integer maxResults;

        public Builder(Collection<String> stopIds) {
            Objects.requireNonNull(stopIds);

            stopIds.forEach(Objects::requireNonNull);

            this.stopIds = List.copyOf(stopIds);
        }

        public Builder routeIds(Collection<String> routeIds) {
            Objects.requireNonNull(routeIds);

            routeIds.forEach(Objects::requireNonNull);

            this.routeIds = List.copyOf(routeIds);

            return this;
        }

        public Builder maxResults(Integer maxResults) {
            Objects.requireNonNull(maxResults);

            if (maxResults <= 0) {
                throw new IllegalArgumentException("maxResults must be positive");
            }

            this.maxResults = maxResults;

            return this;
        }

        public StopsPredictionsQuery build() {
            return new StopsPredictionsQuery(
                this.stopIds,
                this.routeIds,
                this.maxResults
            );
        }
    }
}
