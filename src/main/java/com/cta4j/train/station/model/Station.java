package com.cta4j.train.station.model;

import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

/**
 * Represents a train station.
 *
 * @param stopId the unique stop identifier of this station
 * @param direction the {@link CardinalDirection} of this station
 * @param stopName the stop name of this station
 * @param name the name of this station
 * @param descriptiveName the descriptive name of this station
 * @param mapId the map identifier of this station
 * @param adaAccessible whether this station is ADA accessible
 * @param lines the {@code List} of {@link TrainLine}s that serve this station
 * @param location the {@link Location} of this station
 */
@NullMarked
public record Station(
    String stopId,

    CardinalDirection direction,

    String stopName,

    String name,

    String descriptiveName,

    String mapId,

    boolean adaAccessible,

    List<TrainLine> lines,

    Location location
) {
    /**
     * Constructs a {@code Station}.
     *
     * @param stopId the unique stop identifier of the station
     * @param direction the {@link CardinalDirection} of the station
     * @param stopName the stop name of the station
     * @param name the name of the station
     * @param descriptiveName the descriptive name of the station
     * @param mapId the map identifier of the station
     * @param adaAccessible whether the station is ADA accessible
     * @param lines the {@code List} of {@link TrainLine}s that serve the station
     * @param location the {@link Location} of the station
     */
    public Station {
        Objects.requireNonNull(stopId);
        Objects.requireNonNull(direction);
        Objects.requireNonNull(stopName);
        Objects.requireNonNull(name);
        Objects.requireNonNull(descriptiveName);
        Objects.requireNonNull(mapId);
        Objects.requireNonNull(lines);
        Objects.requireNonNull(location);

        lines.forEach(Objects::requireNonNull);

        lines = List.copyOf(lines);
    }
}
