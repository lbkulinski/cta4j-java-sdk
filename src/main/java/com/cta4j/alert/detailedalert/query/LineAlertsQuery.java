package com.cta4j.alert.detailedalert.query;

import com.cta4j.alert.common.model.AlertTrainLine;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/// Represents a query for detailed train line alerts.
///
/// @param lines the [List] of [AlertTrainLine]s to retrieve alerts for
/// @param activeOnly whether to include only alerts that are currently active
/// @param accessibility whether to include alerts that affect accessible paths in stations
/// @param planned whether to include common planned alerts
/// @param byStartDate the optional date; only alerts with a start date before this date are included
/// @param recentDays the optional number of days; only alerts that started within this many days of today are included
@NullMarked
public record LineAlertsQuery(
    List<AlertTrainLine> lines,
    boolean activeOnly,
    boolean accessibility,
    boolean planned,
    @Nullable LocalDate byStartDate,
    @Nullable Integer recentDays
) {
    /// Constructs a `LineAlertsQuery`.
    ///
    /// @param lines the [List] of [AlertTrainLine]s to retrieve alerts for
    /// @param activeOnly whether to include only alerts that are currently active
    /// @param accessibility whether to include alerts that affect accessible paths in stations
    /// @param planned whether to include common planned alerts
    /// @param byStartDate the optional date; only alerts with a start date before this date are included
    /// @param recentDays the optional number of days; only alerts that started within this many days of today are
    ///                   included
    /// @throws NullPointerException if `lines` is `null`, or if any element of `lines` is `null`
    /// @throws IllegalArgumentException if both `byStartDate` and `recentDays` are specified, or if `recentDays` is
    /// non-`null` and not positive
    public LineAlertsQuery {
        Objects.requireNonNull(lines);

        lines = List.copyOf(lines);

        if (byStartDate != null && recentDays != null) {
            throw new IllegalArgumentException("byStartDate and recentDays cannot both be specified");
        }

        if (recentDays != null && recentDays <= 0) {
            throw new IllegalArgumentException("recentDays must be positive");
        }
    }

    /// Creates a builder for `LineAlertsQuery`.
    ///
    /// @param lines the [Collection] of [AlertTrainLine]s to retrieve alerts for
    /// @return a new `Builder` instance
    /// @throws NullPointerException if `lines` is `null`, or if any element of `lines` is `null`
    public static Builder builder(Collection<AlertTrainLine> lines) {
        return new Builder(lines);
    }

    /// A builder for `LineAlertsQuery`.
    public static final class Builder {
        /// The [List] of [AlertTrainLine]s to retrieve alerts for.
        private final List<AlertTrainLine> lines;

        /// Whether to include only alerts that are currently active.
        private boolean activeOnly;

        /// Whether to include alerts that affect accessible paths in stations.
        private boolean accessibility;

        /// Whether to include common planned alerts.
        private boolean planned;

        /// The optional date; only alerts with a start date before this date are included.
        @Nullable
        private LocalDate byStartDate;

        /// The optional number of days; only alerts that started within this many days of today are included.
        @Nullable
        private Integer recentDays;

        /// Constructs a `Builder`.
        ///
        /// By default, `activeOnly` is `false`, and `accessibility` and `planned` are `true`, matching the CTA Alerts
        /// API's own defaults.
        ///
        /// @param lines the [Collection] of [AlertTrainLine]s to retrieve alerts for
        /// @throws NullPointerException if `lines` is `null`, or if any element of `lines` is `null`
        public Builder(Collection<AlertTrainLine> lines) {
            Objects.requireNonNull(lines);

            this.lines = List.copyOf(lines);
            this.activeOnly = false;
            this.accessibility = true;
            this.planned = true;
        }

        /// Sets whether to include only alerts that are currently active.
        ///
        /// @param activeOnly whether to include only active alerts
        /// @return this `Builder` instance
        public Builder activeOnly(boolean activeOnly) {
            this.activeOnly = activeOnly;

            return this;
        }

        /// Sets whether to include alerts that affect accessible paths in stations.
        ///
        /// @param accessibility whether to include accessibility-related alerts
        /// @return this `Builder` instance
        public Builder accessibility(boolean accessibility) {
            this.accessibility = accessibility;

            return this;
        }

        /// Sets whether to include common planned alerts.
        ///
        /// @param planned whether to include planned alerts
        /// @return this `Builder` instance
        public Builder planned(boolean planned) {
            this.planned = planned;

            return this;
        }

        /// Sets the date; only alerts with a start date before this date are included.
        ///
        /// @param byStartDate the date to filter alerts by
        /// @return this `Builder` instance
        /// @throws NullPointerException if `byStartDate` is `null`
        public Builder byStartDate(LocalDate byStartDate) {
            Objects.requireNonNull(byStartDate);

            this.byStartDate = byStartDate;

            return this;
        }

        /// Sets the number of days; only alerts that started within this many days of today are included.
        ///
        /// @param recentDays the number of days to filter alerts by
        /// @return this `Builder` instance
        /// @throws IllegalArgumentException if `recentDays` is not positive
        public Builder recentDays(int recentDays) {
            if (recentDays <= 0) {
                throw new IllegalArgumentException("recentDays must be positive");
            }

            this.recentDays = recentDays;

            return this;
        }

        /// Builds the `LineAlertsQuery`.
        ///
        /// @return a new `LineAlertsQuery` instance
        /// @throws IllegalArgumentException if both `byStartDate` and `recentDays` were specified
        public LineAlertsQuery build() {
            return new LineAlertsQuery(
                this.lines,
                this.activeOnly,
                this.accessibility,
                this.planned,
                this.byStartDate,
                this.recentDays
            );
        }
    }
}
