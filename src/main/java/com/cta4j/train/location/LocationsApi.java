package com.cta4j.train.location;

import com.cta4j.common.train.TrainLine;
import com.cta4j.train.location.exception.Cta4jLocationsException;
import com.cta4j.train.location.model.TrainLocations;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Provides access to location-related endpoints of the CTA Train Tracker API.
 * <p>
 * This API allows retrieval of train locations by line.
 */
@NullMarked
public interface LocationsApi {
    /**
     * Retrieves train locations for the specified lines.
     *
     * @param lines a {@link List} of {@link TrainLine}s to filter the train locations by
     * @return a {@link List} of {@link TrainLocations} corresponding to the provided lines, or an empty {@link List}
     * if no train locations are found for the specified lines
     * @throws NullPointerException if {@code lines} is {@code null} or contains {@code null} elements
     * @throws Cta4jLocationsException if the API returns an error response or the response cannot be parsed
     */
    List<TrainLocations> findByLines(List<TrainLine> lines);

    /**
     * Retrieves train locations for the specified line.
     *
     * @param line the {@link TrainLine} to filter the train locations by
     * @return a {@link List} of {@link TrainLocations} corresponding to the provided line, or an empty {@link List} if
     * no train locations are found for the specified line
     * @throws NullPointerException if {@code line} is {@code null}
     * @throws Cta4jLocationsException if the API returns an error response or the response cannot be parsed
     */
    default List<TrainLocations> findByLine(TrainLine line) {
        return findByLines(List.of(line));
    }

    /**
     * Retrieves train locations for all lines.
     *
     * @return a {@link List} of {@link TrainLocations} for all lines, or an empty {@link List} if no train locations
     * are found
     * @throws Cta4jLocationsException if the API returns an error response or the response cannot be parsed
     */
    default List<TrainLocations> findAll() {
        return findByLines(List.of(TrainLine.values()));
    }
}
