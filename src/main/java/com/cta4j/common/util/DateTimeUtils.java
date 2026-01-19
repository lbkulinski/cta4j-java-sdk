package com.cta4j.common.util;

import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ApiStatus.Internal
public final class DateTimeUtils {
    private DateTimeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Instant parseTrainTimestamp(String timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }

        ZoneId chicagoId = ZoneId.of("America/Chicago");

        return LocalDateTime.parse(timestamp)
                            .atZone(chicagoId)
                            .toInstant();
    }
}
