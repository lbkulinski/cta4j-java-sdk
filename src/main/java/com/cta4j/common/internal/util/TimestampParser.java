package com.cta4j.common.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class TimestampParser {
    private TimestampParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Instant parse(String timestamp, DateTimeFormatter formatter, ZoneId zoneId) {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(formatter);
        Objects.requireNonNull(zoneId);

        try {
            return LocalDateTime.parse(timestamp, formatter)
                                .atZone(zoneId)
                                .toInstant();
        } catch (DateTimeParseException e) {
            String message = "Failed to parse timestamp: %s".formatted(timestamp);

            throw new IllegalArgumentException(message, e);
        }
    }

    public static @Nullable Instant parseNullable(
        @Nullable String timestamp,
        DateTimeFormatter formatter,
        ZoneId zoneId
    ) {
        Objects.requireNonNull(formatter);
        Objects.requireNonNull(zoneId);

        if (timestamp == null) {
            return null;
        }

        return parse(timestamp, formatter, zoneId);
    }
}
