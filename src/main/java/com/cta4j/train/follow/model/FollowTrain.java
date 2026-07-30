package com.cta4j.train.follow.model;

import com.cta4j.common.geo.Coordinates;
import com.cta4j.train.common.model.Arrival;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/// Represents a response from the "follow" endpoint of the CTA Train Tracker API.
///
/// @param coordinates the current coordinates of this train being followed, if applicable
/// @param arrivals the [List] of [Arrival]s for this train being followed
@NullMarked
public record FollowTrain(
    @Nullable Coordinates coordinates,
    List<Arrival> arrivals
) {
    /// Constructs a `FollowTrain`.
    ///
    /// @param coordinates the current coordinates of the train being followed, if applicable
    /// @param arrivals the [List] of [Arrival]s for the train being followed
    /// @throws NullPointerException if `arrivals` is `null`, or if any element of `arrivals` is `null`
    public FollowTrain {
        Objects.requireNonNull(arrivals);

        arrivals = List.copyOf(arrivals);
    }
}
