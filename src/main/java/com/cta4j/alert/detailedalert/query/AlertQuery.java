package com.cta4j.alert.detailedalert.query;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a query for detailed alerts.
 *
 * @param activeOnly whether to include only alerts that are currently active
 * @param accessibility whether to include alerts that affect accessible paths in stations
 * @param planned whether to include common planned alerts
 * @param byStartDate the optional date; only alerts with a start date before this date are included
 * @param recentDays the optional number of days; only alerts that started within this many days of today are
 *                   included
 */
@NullMarked
public record AlertQuery(
    boolean activeOnly,
    boolean accessibility,
    boolean planned,
    @Nullable LocalDate byStartDate,
    @Nullable Integer recentDays
) {
    /**
     * Constructs an {@code AlertQuery}.
     *
     * @param activeOnly whether to include only alerts that are currently active
     * @param accessibility whether to include alerts that affect accessible paths in stations
     * @param planned whether to include common planned alerts
     * @param byStartDate the optional date; only alerts with a start date before this date are included
     * @param recentDays the optional number of days; only alerts that started within this many days of today are
     *                   included
     * @throws IllegalArgumentException if both {@code byStartDate} and {@code recentDays} are specified, or if
     * {@code recentDays} is non-{@code null} and not positive
     */
    public AlertQuery {
        if (byStartDate != null && recentDays != null) {
            throw new IllegalArgumentException("byStartDate and recentDays cannot both be specified");
        }

        if (recentDays != null && recentDays <= 0) {
            throw new IllegalArgumentException("recentDays must be positive");
        }
    }

    /**
     * Creates a builder for {@code AlertQuery}.
     *
     * @return a new {@code Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@code AlertQuery}.
     */
    public static final class Builder {
        /**
         * Whether to include only alerts that are currently active.
         */
        private boolean activeOnly;

        /**
         * Whether to include alerts that affect accessible paths in stations.
         */
        private boolean accessibility;

        /**
         * Whether to include common planned alerts.
         */
        private boolean planned;

        /**
         * The optional date; only alerts with a start date before this date are included.
         */
        @Nullable
        private LocalDate byStartDate;

        /**
         * The optional number of days; only alerts that started within this many days of today are included.
         */
        @Nullable
        private Integer recentDays;

        /**
         * Constructs a {@code Builder}.
         * <p>
         * By default, {@code activeOnly} is {@code false}, and {@code accessibility} and {@code planned} are
         * {@code true}, matching the CTA Alerts API's own defaults.
         */
        public Builder() {
            this.activeOnly = false;
            this.accessibility = true;
            this.planned = true;
        }

        /**
         * Sets whether to include only alerts that are currently active.
         *
         * @param activeOnly whether to include only active alerts
         * @return this {@code Builder} instance
         */
        public Builder activeOnly(boolean activeOnly) {
            this.activeOnly = activeOnly;

            return this;
        }

        /**
         * Sets whether to include alerts that affect accessible paths in stations.
         *
         * @param accessibility whether to include accessibility-related alerts
         * @return this {@code Builder} instance
         */
        public Builder accessibility(boolean accessibility) {
            this.accessibility = accessibility;

            return this;
        }

        /**
         * Sets whether to include common planned alerts.
         *
         * @param planned whether to include planned alerts
         * @return this {@code Builder} instance
         */
        public Builder planned(boolean planned) {
            this.planned = planned;

            return this;
        }

        /**
         * Sets the date; only alerts with a start date before this date are included.
         *
         * @param byStartDate the date to filter alerts by
         * @return this {@code Builder} instance
         * @throws NullPointerException if {@code byStartDate} is {@code null}
         */
        public Builder byStartDate(LocalDate byStartDate) {
            Objects.requireNonNull(byStartDate);

            this.byStartDate = byStartDate;

            return this;
        }

        /**
         * Sets the number of days; only alerts that started within this many days of today are included.
         *
         * @param recentDays the number of days to filter alerts by
         * @return this {@code Builder} instance
         * @throws IllegalArgumentException if {@code recentDays} is not positive
         */
        public Builder recentDays(int recentDays) {
            if (recentDays <= 0) {
                throw new IllegalArgumentException("recentDays must be positive");
            }

            this.recentDays = recentDays;

            return this;
        }

        /**
         * Builds the {@code AlertQuery}.
         *
         * @return a new {@code AlertQuery} instance
         * @throws IllegalArgumentException if both {@code byStartDate} and {@code recentDays} were specified
         */
        public AlertQuery build() {
            return new AlertQuery(
                this.activeOnly,
                this.accessibility,
                this.planned,
                this.byStartDate,
                this.recentDays
            );
        }
    }
}
