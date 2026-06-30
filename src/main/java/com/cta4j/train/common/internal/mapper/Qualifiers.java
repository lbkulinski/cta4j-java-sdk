package com.cta4j.train.common.internal.mapper;

import com.cta4j.common.geo.Coordinates;
import com.cta4j.train.common.internal.wire.CtaArrival;
import com.cta4j.train.common.model.TrainDirection;
import com.cta4j.train.follow.internal.wire.CtaPosition;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.CardinalDirection;
import com.cta4j.train.station.model.HumanAddress;
import com.cta4j.train.common.model.TrainLine;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Named;
import com.cta4j.exception.Cta4jException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class Qualifiers {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final ZoneId CHICAGO_ZONE_ID = ZoneId.of("America/Chicago");

    private Qualifiers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Named("mapDirection")
    public static CardinalDirection mapDirection(String direction) {
        Objects.requireNonNull(direction);

        return CardinalDirection.fromCode(direction);
    }

    @Named("mapTrainLines")
    public static List<TrainLine> mapTrainLines(CtaStation station) {
        Objects.requireNonNull(station);

        List<TrainLine> lines = new ArrayList<>();

        if (station.red()) {
            lines.add(TrainLine.RED);
        }

        if (station.blue()) {
            lines.add(TrainLine.BLUE);
        }

        if (station.brn()) {
            lines.add(TrainLine.BROWN);
        }

        if (station.g()) {
            lines.add(TrainLine.GREEN);
        }

        if (station.o()) {
            lines.add(TrainLine.ORANGE);
        }

        if (station.p()) {
            lines.add(TrainLine.PURPLE);
        }

        if (station.pnk()) {
            lines.add(TrainLine.PINK);
        }

        if (station.y()) {
            lines.add(TrainLine.YELLOW);
        }

        return List.copyOf(lines);
    }

    @Named("mapHumanAddress")
    public static @Nullable HumanAddress mapHumanAddress(@Nullable String humanAddress) {
        if (humanAddress == null) {
            return null;
        }

        HumanAddress address;

        try {
            address = JsonMapper.shared()
                                .readValue(humanAddress, HumanAddress.class);
        } catch (JacksonException e) {
            String message = "Failed to parse human address: %s".formatted(humanAddress);

            throw new IllegalArgumentException(message, e);
        }

        return address;
    }

    @Named("mapLine")
    public static TrainLine mapLine(String line) {
        Objects.requireNonNull(line);

        return TrainLine.fromCode(line);
    }

    @Named("mapTimestamp")
    public static Instant mapTimestamp(String timestamp) {
        Objects.requireNonNull(timestamp);

        try {
            return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER)
                                .atZone(CHICAGO_ZONE_ID)
                                .toInstant();
        } catch (DateTimeParseException e) {
            String message = "Failed to parse timestamp: %s".formatted(timestamp);

            throw new Cta4jException(message, e);
        }
    }

    @Named("map01ToBoolean")
    public static boolean map01ToBoolean(String value) {
        Objects.requireNonNull(value);

        return switch (value) {
            case "0" -> false;
            case "1" -> true;
            default -> {
                String message = "Invalid boolean value: %s. Expected 0 or 1".formatted(value);

                throw new Cta4jException(message);
            }
        };
    }

    @Named("map15ToTrainDirection")
    public static TrainDirection map15ToTrainDirection(String direction) {
        Objects.requireNonNull(direction);

        int code;

        try {
            code = Integer.parseInt(direction);
        } catch (NumberFormatException e) {
            String message = "Failed to parse train direction: %s".formatted(direction);

            throw new Cta4jException(message, e);
        }

        return TrainDirection.fromCode(code);
    }

    @Named("parseCoordinate")
    public static BigDecimal parseCoordinate(String value) {
        Objects.requireNonNull(value);

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            String message = "Failed to parse coordinate: %s".formatted(value);

            throw new Cta4jException(message, e);
        }
    }

    @Named("parseHeading")
    public static int parseHeading(String value) {
        Objects.requireNonNull(value);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String message = "Failed to parse heading: %s".formatted(value);

            throw new Cta4jException(message, e);
        }
    }

    @Named("mapArrivalCoordinates")
    public static @Nullable Coordinates mapArrivalCoordinates(CtaArrival arrival) {
        Objects.requireNonNull(arrival);

        return mapCoordinates(arrival.lat(), arrival.lon(), arrival.heading());
    }

    @Named("mapCoordinates")
    public static @Nullable Coordinates mapCoordinates(CtaPosition position) {
        Objects.requireNonNull(position);

        return mapCoordinates(position.lat(), position.lon(), position.heading());
    }

    private static @Nullable Coordinates mapCoordinates(
        @Nullable String lat,
        @Nullable String lon,
        @Nullable String heading
    ) {
        if (lat == null) {
            return null;
        }

        if (lon == null) {
            return null;
        }

        if (heading == null) {
            return null;
        }

        BigDecimal latitude;
        BigDecimal longitude;
        int headingValue;

        try {
            latitude = new BigDecimal(lat);
            longitude = new BigDecimal(lon);
            headingValue = Integer.parseInt(heading);
        } catch (NumberFormatException e) {
            String message = "Failed to parse coordinates: lat=%s, lon=%s, heading=%s".formatted(lat, lon, heading);

            throw new Cta4jException(message, e);
        }

        return new Coordinates(latitude, longitude, headingValue);
    }
}
