package com.cta4j.train.station.model;

import java.util.Objects;

/**
 * Represents the four cardinal directions.
 */
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
     * Converts a {@link  String} representation of a cardinal direction to its corresponding enum value.
     *
     * @param direction the {@code String} representation of the cardinal direction
     * @return the corresponding {@code CardinalDirection} enum value
     * @throws NullPointerException if {@code direction} is {@code null}
     * @throws IllegalArgumentException if the provided {@code String} does not correspond to a valid cardinal direction
     */
    public static CardinalDirection fromString(String direction) {
        Objects.requireNonNull(direction);

        return switch (direction.toUpperCase()) {
            case "N", "NORTH" -> NORTH;
            case "E", "EAST" -> EAST;
            case "S", "SOUTH" -> SOUTH;
            case "W", "WEST" -> WEST;
            default -> throw new IllegalArgumentException("Invalid cardinal direction: %s".formatted(direction));
        };
    }
}
