package com.cta4j.train.internal.mapper;

import com.cta4j.internal.json.Cta4jObjectMapper;
import com.cta4j.train.station.internal.wire.CtaLocation;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.CardinalDirection;
import com.cta4j.train.station.model.HumanAddress;
import com.cta4j.train.station.model.Location;
import com.cta4j.train.station.model.TrainLine;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.mapstruct.Named;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class Qualifiers {
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

    @Named("mapLocation")
    public static Location mapLocation(CtaLocation location) {
        Objects.requireNonNull(location);

        ObjectMapper objectMapper = Cta4jObjectMapper.instance();

        BigDecimal latitude = new BigDecimal(location.latitude());
        BigDecimal longitude = new BigDecimal(location.longitude());

        HumanAddress humanAddress;

        try {
            humanAddress = objectMapper.readValue(location.humanAddress(), HumanAddress.class);
        } catch (Exception e) {
            String message = String.format("Failed to parse human address: %s", location.humanAddress());

            throw new IllegalArgumentException(message, e);
        }

        return new Location(latitude, longitude, humanAddress);
    }
}
