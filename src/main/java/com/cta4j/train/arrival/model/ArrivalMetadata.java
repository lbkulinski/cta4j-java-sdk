package com.cta4j.train.arrival.model;

import com.cta4j.internal.geo.GeoConstants;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents metadata associated with a train arrival.
 *
 * @param runNumber the run number of the train associated with this arrival
 * @param direction the direction of travel of the train associated with this arrival
 * @param latitude the latitude of the train associated with this arrival
 * @param longitude the longitude of the train associated with this arrival
 * @param heading the heading of the train associated with this arrival
 * @param flags the flags associated with this arrival, if applicable
 */
@NullMarked
public record ArrivalMetadata(
    String runNumber,

    TrainDirection direction,

    @Nullable
    BigDecimal latitude,

    @Nullable
    BigDecimal longitude,

    @Nullable
    Integer heading,

    @Nullable
    String flags
) {
    /**
     * Constructs an {@code ArrivalMetadata}.
     *
     * @param runNumber the run number of the train associated with the arrival
     * @param direction the direction of travel of the train associated with the arrival
     * @param latitude the latitude of the train associated with the arrival
     * @param longitude the longitude of the train associated with the arrival
     * @param heading the heading of the train associated with the arrival
     * @param flags the flags associated with the arrival, if applicable
     * @throws NullPointerException if {@code runNumber} or {@code direction} is {@code null}
     * @throws IllegalArgumentException if {@code latitude} is not between -90 and 90 (inclusive), if {@code longitude}
     * is not between -180 and 180 (inclusive), or if {@code heading} is not between 0 and 359 (inclusive)
     */
    public ArrivalMetadata {
        Objects.requireNonNull(runNumber);
        Objects.requireNonNull(direction);

        if ((latitude != null) &&
            ((latitude.compareTo(GeoConstants.MIN_LATITUDE) < 0) ||
                (latitude.compareTo(GeoConstants.MAX_LATITUDE) > 0))) {
            String message = String.format(
                "latitude must be between %s and %s (inclusive)",
                GeoConstants.MIN_LATITUDE,
                GeoConstants.MAX_LATITUDE
            );

            throw new IllegalArgumentException(message);
        }

        if ((longitude != null) &&
            ((longitude.compareTo(GeoConstants.MIN_LONGITUDE) < 0) ||
                (longitude.compareTo(GeoConstants.MAX_LONGITUDE) > 0))) {
            String message = String.format(
                "longitude must be between %s and %s (inclusive)",
                GeoConstants.MIN_LONGITUDE,
                GeoConstants.MAX_LONGITUDE
            );

            throw new IllegalArgumentException(message);
        }

        if ((heading != null) &&
            ((heading < GeoConstants.MIN_HEADING) ||
                (heading > GeoConstants.MAX_HEADING))) {
            String message = String.format(
                "heading must be between %d and %d (inclusive)",
                GeoConstants.MIN_HEADING,
                GeoConstants.MAX_HEADING
            );

            throw new IllegalArgumentException(message);
        }
    }
}
