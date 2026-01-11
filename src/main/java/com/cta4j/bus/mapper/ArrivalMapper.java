package com.cta4j.bus.mapper;

import com.cta4j.bus.external.CtaPrediction;
import com.cta4j.bus.model.Arrival;
import com.cta4j.bus.model.DynamicAction;
import com.cta4j.bus.model.FlagStop;
import com.cta4j.bus.model.PassengerLoad;
import com.cta4j.bus.model.PredictionType;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper
@ApiStatus.Internal
public interface ArrivalMapper {
    @Mapping(source = "typ", target = "predictionType", qualifiedByName = "mapPredictionType")
    @Mapping(source = "stpid", target = "stopId")
    @Mapping(source = "stpnm", target = "stopName")
    @Mapping(source = "vid", target = "vehicleId")
    @Mapping(source = "dstp", target = "distanceToStop")
    @Mapping(source = "rt", target = "route")
    @Mapping(source = "rtdd", target = "routeDesignator")
    @Mapping(source = "rtdir", target = "routeDirection")
    @Mapping(source = "des", target = "destination")
    @Mapping(source = "prdtm", target = "arrivalTime", qualifiedByName = "mapTimestamp")
    @Mapping(source = "dly", target = "delayed")
    @Mapping(source = "tmstmp", target = "metadata.timestamp", qualifiedByName = "mapTimestamp")
    @Mapping(source = "dyn", target = "metadata.dynamicAction", qualifiedByName = "mapDynamicAction")
    @Mapping(source = "tablockid", target = "metadata.blockId")
    @Mapping(source = "tatripid", target = "metadata.tripId")
    @Mapping(source = "origtatripno", target = "metadata.originalTripNumber")
    @Mapping(source = "zone", target = "metadata.zone")
    @Mapping(source = "psgld", target = "metadata.passengerLoad", qualifiedByName = "mapPassengerLoad")
    @Mapping(source = "gtfsseq", target = "metadata.gtfsSequence")
    @Mapping(source = "nbus", target = "metadata.nextBus")
    @Mapping(source = "stst", target = "metadata.scheduledStartTimeSeconds")
    @Mapping(source = "stsd", target = "metadata.scheduledStartDate")
    @Mapping(source = "flagstop", target = "metadata.flagStop", qualifiedByName = "mapFlagStop")
    Arrival toDomain(CtaPrediction prediction);

    @Named("mapPredictionType")
    static PredictionType mapPredictionType(String typ) {
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

    DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");

    @Named("mapTimestamp")
    static Instant mapTimestamp(String timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }

        return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER)
                            .atZone(ZoneOffset.UTC)
                            .toInstant();
    }

    @Named("mapDynamicAction")
    static DynamicAction mapDynamicAction(int dyn) {
        for (DynamicAction dynamicAction : DynamicAction.values()) {
            if (dynamicAction.getCode() == dyn) {
                return dynamicAction;
            }
        }

        String message = String.format("Unknown dynamic action code: %d", dyn);

        throw new IllegalArgumentException(message);
    }

    @Named("mapPassengerLoad")
    static PassengerLoad mapPassengerLoad(String psgld) {
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
    static FlagStop mapFlagStop(int flagstop) {
        for (FlagStop flagStop : FlagStop.values()) {
            if (flagStop.getCode() == flagstop) {
                return flagStop;
            }
        }

        String message = String.format("Unknown flag stop code: %d", flagstop);

        throw new IllegalArgumentException(message);
    }
}
