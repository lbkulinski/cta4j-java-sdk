package com.cta4j.train.station.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/// Represents the four cardinal directions.
@NullMarked
public enum CardinalDirection {
    /// Indicates the north direction.
    NORTH,

    /// Indicates the east direction.
    EAST,

    /// Indicates the south direction.
    SOUTH,

    /// Indicates the west direction.
    WEST;

    /// Returns the `CardinalDirection` corresponding to the given code.
    ///
    /// @param code the code representing the cardinal direction (e.g., "N", "E", "S", "W" or their full names)
    /// @return the corresponding `CardinalDirection`
    /// @throws IllegalArgumentException if the code does not correspond to any known cardinal direction
    public static CardinalDirection fromCode(String code) {
        Objects.requireNonNull(code);

        return switch (code.toUpperCase()) {
            case "N", "NORTH" -> NORTH;
            case "E", "EAST" -> EAST;
            case "S", "SOUTH" -> SOUTH;
            case "W", "WEST" -> WEST;
            default -> throw new IllegalArgumentException("Invalid cardinal direction code: %s".formatted(code));
        };
    }
}
