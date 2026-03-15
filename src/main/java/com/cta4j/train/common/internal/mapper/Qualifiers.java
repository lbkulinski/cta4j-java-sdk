package com.cta4j.train.common.internal.mapper;

import com.cta4j.common.geo.Coordinates;
import com.cta4j.common.internal.json.Cta4jObjectMapper;
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
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
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

        ObjectMapper objectMapper = Cta4jObjectMapper.instance();

        HumanAddress address;

        try {
            address = objectMapper.readValue(humanAddress, HumanAddress.class);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse human address: %s", humanAddress);

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

        return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER)
                            .atZone(CHICAGO_ZONE_ID)
                            .toInstant();
    }

    @Named("map01ToBoolean")
    public static boolean map01ToBoolean(int value) {
        return switch (value) {
            case 0 -> false;
            case 1 -> true;
            default -> {
                String message = String.format("Invalid boolean value: %s. Expected 0 or 1", value);

                throw new IllegalArgumentException(message);
            }
        };
    }

    @Named("map15ToTrainDirection")
    public static TrainDirection map15ToTrainDirection(int direction) {
        return TrainDirection.fromCode(direction);
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
        @Nullable Double lat,
        @Nullable Double lon,
        @Nullable Integer heading
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

        BigDecimal latitude = BigDecimal.valueOf(lat);
        BigDecimal longitude = BigDecimal.valueOf(lon);

        return new Coordinates(latitude, longitude, heading);
    }
}
