package com.cta4j.mapper.train;

import com.cta4j.model.train.Route;

import java.util.Objects;

public final class RouteMapper {
    private RouteMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Route fromExternal(String string) {
        Objects.requireNonNull(string);

        string = string.toUpperCase();

        return switch (string) {
            case "RED", "RED LINE" -> Route.RED;
            case "BLUE", "BLUE LINE" -> Route.BLUE;
            case "BRN", "BROWN LINE" -> Route.BROWN;
            case "G", "GREEN LINE" -> Route.GREEN;
            case "ORG", "ORANGE LINE" -> Route.ORANGE;
            case "P", "PURPLE LINE" -> Route.PURPLE;
            case "PINK", "PINK LINE" -> Route.PINK;
            case "Y", "YELLOW LINE" -> Route.YELLOW;
            case "N/A" -> Route.N_A;
            default -> {
                String message = "A line with the name \"%s\" does not exist".formatted(string);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
