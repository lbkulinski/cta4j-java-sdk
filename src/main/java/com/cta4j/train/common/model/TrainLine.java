package com.cta4j.train.common.model;

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
    RED("Red", "#C60C30"),

    /**
     * The Blue Line.
     */
    BLUE("Blue", "#00A1DE"),

    /**
     * The Brown Line.
     */
    BROWN("Brn", "#62361B"),

    /**
     * The Green Line.
     */
    GREEN("G", "#009B3A"),

    /**
     * The Orange Line.
     */
    ORANGE("Org", "#F9461C"),

    /**
     * The Purple Line.
     */
    PURPLE("P", "#522398"),

    /**
     * The Pink Line.
     */
    PINK("Pink", "#E27EA6"),

    /**
     * The Yellow Line.
     */
    YELLOW("Y", "#F9E300");

    /**
     * The CTA code for this train line.
     */
    private final String code;

    /**
     * The hex color code of this train line.
     */
    private final String colorHex;

    /**
     * Constructs a {@code TrainLine}.
     *
     * @param code the CTA code of the train line
     * @param colorHex the hex color code of the train line
     * @throws NullPointerException if {@code code} or {@code colorHex} is {@code null}
     */
    TrainLine(String code, String colorHex) {
        this.code = Objects.requireNonNull(code);
        this.colorHex = Objects.requireNonNull(colorHex);
    }

    /**
     * Gets the CTA code for this train line.
     *
     * @return the CTA code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Gets the hex color code of this train line.
     *
     * @return the hex color code
     */
    public String getColorHex() {
        return this.colorHex;
    }

    /**
     * Returns the {@code TrainLine} corresponding to the given code.
     *
     * @param code the CTA code of the train line (case-insensitive, may include "LINE" suffix)
     * @return the corresponding {@code TrainLine}
     * @throws IllegalArgumentException if the code does not correspond to any known train line
     */
    public static TrainLine fromCode(String code) {
        Objects.requireNonNull(code);

        return switch (code.toUpperCase()) {
            case "RED", "RED LINE" -> TrainLine.RED;
            case "BLUE", "BLUE LINE" -> TrainLine.BLUE;
            case "BRN", "BROWN LINE" -> TrainLine.BROWN;
            case "G", "GREEN LINE" -> TrainLine.GREEN;
            case "ORG", "ORANGE LINE" -> TrainLine.ORANGE;
            case "P", "PURPLE LINE" -> TrainLine.PURPLE;
            case "PINK", "PINK LINE" -> TrainLine.PINK;
            case "Y", "YELLOW LINE" -> TrainLine.YELLOW;
            default -> throw new IllegalArgumentException("Invalid train line: %s".formatted(code));
        };
    }
}
