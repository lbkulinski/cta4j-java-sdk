package com.cta4j.common.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class BooleanParser {
    private BooleanParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean parse01(String value) {
        Objects.requireNonNull(value);

        return switch (value) {
            case "0" -> false;
            case "1" -> true;
            default -> {
                String message = "Invalid value: %s. Expected 0 or 1".formatted(value);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
