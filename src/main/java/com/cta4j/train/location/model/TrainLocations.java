package com.cta4j.train.location.model;

import com.cta4j.train.common.model.TrainLine;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

/**
 * Represents the locations of all trains on a route.
 *
 * @param line the train line associated with these locations
 * @param trains the {@link List} of {@link LocationTrain}s for this train line
 */
@NullMarked
public record TrainLocations(
    TrainLine line,

    List<LocationTrain> trains
) {
    /**
     * Constructs a {@code TrainLocations}.
     *
     * @param line the train line associated with the locations
     * @param trains the {@link List} of {@link LocationTrain}s for the train line
     * @throws NullPointerException if {@code line} or {@code trains} is {@code null}, or if {@code trains} contains
     * {@code null} elements
     */
    public TrainLocations {
        Objects.requireNonNull(line);
        Objects.requireNonNull(trains);

        trains = List.copyOf(trains);
    }
}
