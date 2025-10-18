package com.cta4j.bus.mapper;

import com.cta4j.bus.model.BusPredictionType;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.Internal
public final class BusPredictionTypeMapper {
    private BusPredictionTypeMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static BusPredictionType fromExternal(String string) {
        Objects.requireNonNull(string);

        string = string.toUpperCase();

        return switch (string) {
            case "A" -> BusPredictionType.ARRIVAL;
            case "D" -> BusPredictionType.DEPARTURE;
            default -> {
                String message = "A bus prediction type with the name \"%s\" does not exist".formatted(string);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
