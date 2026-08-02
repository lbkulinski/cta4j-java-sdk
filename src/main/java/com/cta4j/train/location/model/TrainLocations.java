package com.cta4j.train.location.model;

import com.cta4j.train.common.model.TrainLine;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/// Represents the locations of all trains on a route.
///
/// @param line the train line associated with these locations, if applicable
/// @param trains the [List] of [LocationTrain]s for this train line
@NullMarked
public record TrainLocations(
    @Nullable TrainLine line,
    List<LocationTrain> trains
) {
    /// Constructs a `TrainLocations`.
    ///
    /// @param line the train line associated with the locations, if applicable
    /// @param trains the [List] of [LocationTrain]s for the train line
    /// @throws NullPointerException if `trains` is `null`, or if any element of `trains` is `null`
    public TrainLocations {
        Objects.requireNonNull(trains);

        trains = List.copyOf(trains);
    }
}
