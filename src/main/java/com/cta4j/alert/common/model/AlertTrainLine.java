package com.cta4j.alert.common.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/// Represents a train line as filterable through the CTA Alerts API.
///
/// Unlike the Train Tracker API, which has no concept of express service, the Alerts API treats the Purple Line
/// Express as a distinct route designator (`"Pexp"`) from the regular Purple Line (`"P"`); per CTA's documentation,
/// alerts affecting the Purple Line may be tagged with either designator, or both.
@NullMarked
public enum AlertTrainLine {
    /// Indicates the Red Line.
    RED("Red"),

    /// Indicates the Blue Line.
    BLUE("Blue"),

    /// Indicates the Brown Line.
    BROWN("Brn"),

    /// Indicates the Green Line.
    GREEN("G"),

    /// Indicates the Orange Line.
    ORANGE("Org"),

    /// Indicates the Purple Line, excluding express service.
    PURPLE("P"),

    /// Indicates the Purple Line Express.
    PURPLE_EXPRESS("Pexp"),

    /// Indicates the Pink Line.
    PINK("Pink"),

    /// Indicates the Yellow Line.
    YELLOW("Y");

    private final String code;

    AlertTrainLine(String code) {
        this.code = Objects.requireNonNull(code);
    }

    /// Returns the CTA Alerts API route designator for this train line.
    ///
    /// @return the route designator
    public String getCode() {
        return this.code;
    }

    /// Returns the `AlertTrainLine` corresponding to the given route designator.
    ///
    /// @param code the CTA Alerts API route designator of the train line (case-insensitive)
    /// @return the corresponding `AlertTrainLine`
    /// @throws IllegalArgumentException if the code does not correspond to any known train line
    public static AlertTrainLine fromCode(String code) {
        Objects.requireNonNull(code);

        return switch (code.toUpperCase()) {
            case "RED" -> AlertTrainLine.RED;
            case "BLUE" -> AlertTrainLine.BLUE;
            case "BRN" -> AlertTrainLine.BROWN;
            case "G" -> AlertTrainLine.GREEN;
            case "ORG" -> AlertTrainLine.ORANGE;
            case "P" -> AlertTrainLine.PURPLE;
            case "PEXP" -> AlertTrainLine.PURPLE_EXPRESS;
            case "PINK" -> AlertTrainLine.PINK;
            case "Y" -> AlertTrainLine.YELLOW;
            default -> throw new IllegalArgumentException("Invalid alert train line: %s".formatted(code));
        };
    }
}
