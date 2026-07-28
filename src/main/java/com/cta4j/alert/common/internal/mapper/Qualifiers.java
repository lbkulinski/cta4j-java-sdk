package com.cta4j.alert.common.internal.mapper;

import com.cta4j.alert.common.model.ServiceType;
import com.cta4j.common.internal.util.BooleanParser;
import com.cta4j.common.internal.util.TimestampParser;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Named;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class Qualifiers {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd")
        .optionalStart()
        .appendPattern("'T'HH:mm:ss")
        .optionalEnd()
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .toFormatter();

    private static final ZoneId CHICAGO_ZONE_ID = ZoneId.of("America/Chicago");

    private Qualifiers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Named("mapUri")
    public static URI mapUri(String value) {
        Objects.requireNonNull(value);

        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            String message = "Failed to parse URI: %s".formatted(value);

            throw new IllegalArgumentException(message, e);
        }
    }

    @Named("mapTimestamp")
    public static @Nullable Instant mapTimestamp(@Nullable String timestamp) {
        return TimestampParser.parseNullable(timestamp, TIMESTAMP_FORMATTER, CHICAGO_ZONE_ID);
    }

    @Named("map01ToBoolean")
    public static boolean map01ToBoolean(String value) {
        Objects.requireNonNull(value);

        return BooleanParser.parse01(value);
    }

    @Named("mapScore")
    public static int mapScore(String value) {
        Objects.requireNonNull(value);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String message = "Failed to parse score: %s".formatted(value);

            throw new IllegalArgumentException(message, e);
        }
    }

    @Named("mapServiceType")
    public static ServiceType mapServiceType(String serviceType) {
        Objects.requireNonNull(serviceType);

        return switch (serviceType) {
            case "B" -> ServiceType.BUS;
            case "R" -> ServiceType.RAIL;
            case "T" -> ServiceType.STATION;
            case "X" -> ServiceType.SYSTEMWIDE;
            default -> {
                String message = "Unknown service type: %s".formatted(serviceType);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
