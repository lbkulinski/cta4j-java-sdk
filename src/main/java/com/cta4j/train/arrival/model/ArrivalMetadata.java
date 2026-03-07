package com.cta4j.train.arrival.model;

import com.cta4j.train.station.model.CardinalDirection;
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

    CardinalDirection direction,

    BigDecimal latitude,

    BigDecimal longitude,

    int heading,

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
     * @throws NullPointerException if {@code runNumber}, {@code direction}, {@code latitude}, or {@code longitude} is
     * {@code null}
     */
    public ArrivalMetadata {
        Objects.requireNonNull(runNumber);
        Objects.requireNonNull(direction);
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);
    }
}
