package com.cta4j.train.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * A train currently in service.
 *
 * @param coordinates the coordinates and heading of the train
 * @param arrivals the list of upcoming train arrivals for the train
 */
@NullMarked
@SuppressWarnings("ConstantConditions")
public record Train(
    @Nullable
    TrainCoordinates coordinates,

    List<UpcomingTrainArrival> arrivals
) {
    public Train {
        if (arrivals == null) {
            throw new IllegalArgumentException("arrivals must not be null");
        }

        for (UpcomingTrainArrival arrival : arrivals) {
            if (arrival == null) {
                throw new IllegalArgumentException("arrivals must not contain null elements");
            }
        }
    }
}
