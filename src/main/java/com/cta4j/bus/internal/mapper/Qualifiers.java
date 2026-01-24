package com.cta4j.bus.internal.mapper;

import com.cta4j.bus.prediction.model.DynamicAction;
import com.cta4j.bus.prediction.model.FlagStop;
import com.cta4j.bus.prediction.model.PassengerLoad;
import com.cta4j.bus.pattern.model.PatternPointType;
import com.cta4j.bus.prediction.model.PredictionType;
import com.cta4j.bus.vehicle.model.TransitMode;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class Qualifiers {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm[:ss]");
    private static final ZoneId CHICAGO_ZONE_ID = ZoneId.of("America/Chicago");

    private Qualifiers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Named("mapPredictionType")
    public static PredictionType mapPredictionType(String typ) {
        Objects.requireNonNull(typ);

        return switch (typ) {
            case "A" -> PredictionType.ARRIVAL;
            case "D" -> PredictionType.DEPARTURE;
            default -> {
                String message = String.format("Unknown prediction type: %s", typ);

                throw new IllegalArgumentException(message);
            }
        };
    }

    @Named("mapTimestamp")
    public static @Nullable Instant mapTimestamp(@Nullable String timestamp) {
        if (timestamp == null) {
            return null;
        }

        return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER)
                            .atZone(CHICAGO_ZONE_ID)
                            .toInstant();
    }

    @Named("mapDynamicAction")
    public static DynamicAction mapDynamicAction(int dyn) {
        for (DynamicAction dynamicAction : DynamicAction.values()) {
            if (dynamicAction.getCode() == dyn) {
                return dynamicAction;
            }
        }

        String message = String.format("Unknown dynamic action code: %d", dyn);

        throw new IllegalArgumentException(message);
    }

    @Named("mapPassengerLoad")
    public static PassengerLoad mapPassengerLoad(String psgld) {
        Objects.requireNonNull(psgld);

        return switch (psgld) {
            case "FULL" -> PassengerLoad.FULL;
            case "HALF_EMPTY" -> PassengerLoad.HALF_EMPTY;
            case "EMPTY" -> PassengerLoad.EMPTY;
            case "N/A", "" -> PassengerLoad.UNKNOWN;
            default -> {
                String message = String.format("Unknown passenger load: %s", psgld);

                throw new IllegalArgumentException(message);
            }
        };
    }

    @Named("mapFlagStop")
    public static FlagStop mapFlagStop(int flagstop) {
        for (FlagStop flagStop : FlagStop.values()) {
            if (flagStop.getCode() == flagstop) {
                return flagStop;
            }
        }

        String message = String.format("Unknown flag stop code: %d", flagstop);

        throw new IllegalArgumentException(message);
    }

    @Named("mapTransitMode")
    public static TransitMode mapTransitMode(int mode) {
        for (TransitMode transitMode : TransitMode.values()) {
            if (transitMode.getCode() == mode) {
                return transitMode;
            }
        }

        String message = String.format("Unknown transit mode code: %d", mode);

        throw new IllegalArgumentException(message);
    }

    @Named("mapActive")
    public static boolean mapActive(int st) {
        return st == 1;
    }

    @Named("mapPatternPointType")
    public static PatternPointType mapPatternPointType(String type) {
        Objects.requireNonNull(type);

        return switch (type) {
            case "S" -> PatternPointType.STOP;
            case "W" -> PatternPointType.WAYPOINT;
            default -> {
                String message = String.format("Unknown pattern point type: %s", type);

                throw new IllegalArgumentException(message);
            }
        };
    }

    @Named("mapLocale")
    public static Locale mapLocale(String locale) {
        Objects.requireNonNull(locale);

        return Locale.of(locale);
    }
}
