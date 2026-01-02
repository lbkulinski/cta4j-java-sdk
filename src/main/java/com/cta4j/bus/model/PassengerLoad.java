package com.cta4j.bus.model;

public enum PassengerLoad {
    FULL,
    HALF_EMPTY,
    EMPTY,
    UNKNOWN;

    public static PassengerLoad fromString(String value) {
        String upperCaseValue = value.toUpperCase();

        return switch (upperCaseValue) {
            case "FULL" -> FULL;
            case "HALF_EMPTY" -> HALF_EMPTY;
            case "EMPTY" -> EMPTY;
            default -> UNKNOWN;
        };
    }
}
