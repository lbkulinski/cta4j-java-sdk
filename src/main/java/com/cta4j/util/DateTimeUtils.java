package com.cta4j.util;

import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@ApiStatus.Internal
public final class DateTimeUtils {
    private DateTimeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Instant parseTrainTimestamp(String timestamp) {
        Objects.requireNonNull(timestamp);

        ZoneId chicagoId = ZoneId.of("America/Chicago");

        return LocalDateTime.parse(timestamp)
                            .atZone(chicagoId)
                            .toInstant();
    }

    public static Instant parseBusTimestamp(String timestamp) {
        Objects.requireNonNull(timestamp);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");

        ZoneId chicagoId = ZoneId.of("America/Chicago");

        return LocalDateTime.parse(timestamp, formatter)
                            .atZone(chicagoId)
                            .toInstant();
    }
}
