package com.cta4j.train.station.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Represents the four cardinal directions.
 */
@NullMarked
public enum CardinalDirection {
    /**
     * North direction.
     */
    NORTH,

    /**
     * East direction.
     */
    EAST,

    /**
     * South direction.
     */
    SOUTH,

    /**
     * West direction.
     */
    WEST;

    /**
     * Returns the {@code CardinalDirection} corresponding to the given code.
     *
     * @param code the code representing the cardinal direction (e.g., "N", "E", "S", "W" or their full names)
     * @return the corresponding {@code CardinalDirection}
     * @throws IllegalArgumentException if the code does not correspond to any known cardinal direction
     */
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
