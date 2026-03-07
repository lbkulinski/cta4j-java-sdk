package com.cta4j.train.internal.mapper;

import com.cta4j.internal.json.Cta4jObjectMapper;
import com.cta4j.train.arrival.model.TrainDirection;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.CardinalDirection;
import com.cta4j.train.station.model.HumanAddress;
import com.cta4j.train.common.model.TrainLine;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.mapstruct.Named;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class Qualifiers {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final ZoneId CHICAGO_ZONE_ID = ZoneId.of("America/Chicago");

    private Qualifiers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Named("mapDirection")
    public static CardinalDirection mapDirection(String direction) {
        Objects.requireNonNull(direction);

        return switch (direction.toUpperCase()) {
            case "N", "NORTH" -> CardinalDirection.NORTH;
            case "E", "EAST" -> CardinalDirection.EAST;
            case "S", "SOUTH" -> CardinalDirection.SOUTH;
            case "W", "WEST" -> CardinalDirection.WEST;
            default -> throw new IllegalArgumentException("Invalid cardinal direction: %s".formatted(direction));
        };
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
    public static HumanAddress mapHumanAddress(String humanAddress) {
        Objects.requireNonNull(humanAddress);

        ObjectMapper objectMapper = Cta4jObjectMapper.instance();

        HumanAddress address;

        try {
            address = objectMapper.readValue(humanAddress, HumanAddress.class);
        } catch (Exception e) {
            String message = String.format("Failed to parse human address: %s", humanAddress);

            throw new IllegalArgumentException(message, e);
        }

        return address;
    }

    @Named("mapLine")
    public static TrainLine mapLine(String line) {
        Objects.requireNonNull(line);

        return switch (line.toUpperCase()) {
            case "RED" -> TrainLine.RED;
            case "BLUE" -> TrainLine.BLUE;
            case "BRN" -> TrainLine.BROWN;
            case "G" -> TrainLine.GREEN;
            case "ORG" -> TrainLine.ORANGE;
            case "P" -> TrainLine.PURPLE;
            case "PINK" -> TrainLine.PINK;
            case "Y" -> TrainLine.YELLOW;
            default -> throw new IllegalArgumentException("Invalid train line: %s".formatted(line));
        };
    }

    @Named("mapTimestamp")
    public static Instant mapTimestamp(String timestamp) {
        Objects.requireNonNull(timestamp);

        return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER)
                            .atZone(CHICAGO_ZONE_ID)
                            .toInstant();
    }

    @Named("map01ToBoolean")
    public static boolean map01ToBoolean(String value) {
        Objects.requireNonNull(value);

        return switch (value) {
            case "0" -> false;
            case "1" -> true;
            default -> {
                String message = String.format("Invalid boolean value: %s. Expected '0' or '1'.", value);

                throw new IllegalArgumentException(message);
            }
        };
    }

    @Named("map15ToTrainDirection")
    public static TrainDirection map15ToTrainDirection(int direction) {
        return switch (direction) {
            case 1 -> TrainDirection.NORTHBOUND;
            case 5 -> TrainDirection.SOUTHBOUND;
            default -> {
                String message = String.format("Invalid train direction value: %s. Expected 1 or 5", direction);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
