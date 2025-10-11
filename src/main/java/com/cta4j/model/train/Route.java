package com.cta4j.model.train;

import java.util.Objects;

public enum Route {
    RED,

    BLUE,

    BROWN,

    GREEN,

    ORANGE,

    PURPLE,

    PINK,

    YELLOW,

    N_A;

    public static Route parseString(String string) {
        Objects.requireNonNull(string);

        string = string.toUpperCase();

        return switch (string) {
            case "RED", "RED LINE" -> RED;
            case "BLUE", "BLUE LINE" -> BLUE;
            case "BRN", "BROWN LINE" -> BROWN;
            case "G", "GREEN LINE" -> GREEN;
            case "ORG", "ORANGE LINE" -> ORANGE;
            case "P", "PURPLE LINE" -> PURPLE;
            case "PINK", "PINK LINE" -> PINK;
            case "Y", "YELLOW LINE" -> YELLOW;
            case "N/A" -> N_A;
            default -> {
                String message = "A line with the name \"%s\" does not exist".formatted(string);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
