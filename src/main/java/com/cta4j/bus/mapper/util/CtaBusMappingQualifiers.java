package com.cta4j.bus.mapper.util;

import com.cta4j.bus.api.prediction.model.DynamicAction;
import com.cta4j.bus.api.prediction.model.FlagStop;
import com.cta4j.bus.api.prediction.model.PassengerLoad;
import com.cta4j.bus.api.pattern.model.PatternPointType;
import com.cta4j.bus.api.prediction.model.PredictionType;
import com.cta4j.bus.api.vehicle.model.TransitMode;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@ApiStatus.Internal
public final class CtaBusMappingQualifiers {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
    private static final ZoneId CHICAGO_ZONE_ID = ZoneId.of("America/Chicago");

    private CtaBusMappingQualifiers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Named("mapPredictionType")
    public static PredictionType mapPredictionType(String typ) {
        if (typ == null) {
            throw new IllegalArgumentException("typ must not be null");
        }

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
    public static Instant mapTimestamp(String timestamp) {
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
        if (psgld == null) {
            throw new IllegalArgumentException("psgld must not be null");
        }

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
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }

        return switch (type) {
            case "S" -> PatternPointType.STOP;
            case "W" -> PatternPointType.WAYPOINT;
            default -> {
                String message = String.format("Unknown pattern point type: %s", type);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
