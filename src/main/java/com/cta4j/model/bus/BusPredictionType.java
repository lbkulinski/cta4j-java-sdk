package com.cta4j.model.bus;

import java.util.Objects;

public enum BusPredictionType {
    ARRIVAL,

    DEPARTURE;

    public static BusPredictionType parseString(String string) {
        Objects.requireNonNull(string);

        string = string.toUpperCase();

        return switch (string) {
            case "A" -> ARRIVAL;
            case "D" -> DEPARTURE;
            default -> {
                String message = "A bus prediction type with the name \"%s\" does not exist".formatted(string);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
