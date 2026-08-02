package com.cta4j.train.location;

import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.location.exception.Cta4jLocationsException;
import com.cta4j.train.location.model.TrainLocations;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

/// Provides access to location-related endpoints of the CTA Train Tracker API.
///
/// This API allows retrieval of train locations by line.
@NullMarked
public interface LocationsApi {
    /// Retrieves train locations for all lines.
    ///
    /// @return a [List] of [TrainLocations] for all lines, or an empty [List] if no train locations are found
    /// @throws Cta4jLocationsException if the API returns an error response or the response cannot be parsed
    default List<TrainLocations> list() {
        List<TrainLine> lines = List.of(TrainLine.values());

        return this.findByLines(lines);
    }

    /// Retrieves train locations for the specified lines.
    ///
    /// @param lines a [List] of [TrainLine]s to filter the train locations by
    /// @return a [List] of [TrainLocations] corresponding to the provided lines, or an empty [List] if no train
    /// locations are found for the specified lines
    /// @throws NullPointerException if `lines` is `null`, or if any element of `lines` is `null`
    /// @throws Cta4jLocationsException if the API returns an error response or the response cannot be parsed
    List<TrainLocations> findByLines(List<TrainLine> lines);

    /// Retrieves train locations for the specified line.
    ///
    /// @param line the [TrainLine] to filter the train locations by
    /// @return a [List] of [TrainLocations] corresponding to the provided line, or an empty [List] if no train
    /// locations are found for the specified line
    /// @throws NullPointerException if `line` is `null`
    /// @throws Cta4jLocationsException if the API returns an error response or the response cannot be parsed
    default List<TrainLocations> findByLine(TrainLine line) {
        Objects.requireNonNull(line);

        List<TrainLine> lines = List.of(line);

        return this.findByLines(lines);
    }
}
