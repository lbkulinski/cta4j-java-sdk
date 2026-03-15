package com.cta4j.train.common.model;

import com.cta4j.common.geo.Coordinates;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Represents metadata associated with a train arrival.
 *
 * @param runNumber the run number of the train associated with this arrival
 * @param direction the direction of travel of the train associated with this arrival
 * @param coordinates the coordinates of the train associated with this arrival, if applicable
 * @param flags the flags associated with this arrival, if applicable
 */
@NullMarked
public record ArrivalMetadata(
    String runNumber,

    TrainDirection direction,

    @Nullable
    Coordinates coordinates,

    @Nullable
    String flags
) {
    /**
     * Constructs an {@code ArrivalMetadata}.
     *
     * @param runNumber the run number of the train associated with the arrival
     * @param direction the direction of travel of the train associated with the arrival
     * @param coordinates the coordinates of the train associated with the arrival, if applicable
     * @param flags the flags associated with the arrival, if applicable
     * @throws NullPointerException if {@code runNumber} or {@code direction} is {@code null}
     */
    public ArrivalMetadata {
        Objects.requireNonNull(runNumber);
        Objects.requireNonNull(direction);
    }
}
