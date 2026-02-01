package com.cta4j.train.station.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Represents a train line.
 */
@NullMarked
public enum TrainLine {
    /**
     * The Red Line.
     */
    RED("#C60C30"),

    /**
     * The Blue Line.
     */
    BLUE("#00A1DE"),

    /**
     * The Brown Line.
     */
    BROWN("#62361B"),

    /**
     * The Green Line.
     */
    GREEN("#009B3A"),

    /**
     * The Orange Line.
     */
    ORANGE("#F9461C"),

    /**
     * The Purple Line.
     */
    PURPLE("#522398"),

    /**
     * The Pink Line.
     */
    PINK("#E27EA6"),

    /**
     * The Yellow Line.
     */
    YELLOW("#F9E300");

    private final String colorHex;

    TrainLine(String colorHex) {
        this.colorHex = Objects.requireNonNull(colorHex);
    }

    /**
     * Gets the hex color code of this train line.
     *
     * @return the hex color code
     */
    public String getColorHex() {
        return this.colorHex;
    }
}
