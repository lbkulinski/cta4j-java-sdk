package com.cta4j.bus.prediction.internal.mapper;

import com.cta4j.bus.prediction.internal.wire.CtaPrediction;
import com.cta4j.bus.common.internal.mapper.Qualifiers;
import com.cta4j.bus.prediction.model.Prediction;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface PredictionMapper {
    PredictionMapper INSTANCE = Mappers.getMapper(PredictionMapper.class);

    @Mapping(target = "predictionType", source = "typ", qualifiedByName = "mapPredictionType")
    @Mapping(target = "stopId", source = "stpid")
    @Mapping(target = "stopName", source = "stpnm")
    @Mapping(target = "vehicleId", source = "vid")
    @Mapping(target = "distanceToStop", source = "dstp")
    @Mapping(target = "route", source = "rt")
    @Mapping(target = "routeDesignator", source = "rtdd")
    @Mapping(target = "routeDirection", source = "rtdir")
    @Mapping(target = "destination", source = "des")
    @Mapping(target = "arrivalTime", source = "prdtm", qualifiedByName = "mapTimestamp")
    @Mapping(target = "delayed", source = "dly")
    @Mapping(target = "metadata.timestamp", source = "tmstmp", qualifiedByName = "mapTimestamp")
    @Mapping(target = "metadata.dynamicAction", source = "dyn", qualifiedByName = "mapDynamicAction")
    @Mapping(target = "metadata.blockId", source = "tablockid")
    @Mapping(target = "metadata.tripId", source = "tatripid")
    @Mapping(target = "metadata.originalTripNumber", source = "origtatripno")
    @Mapping(target = "metadata.countdownLabel", source = "prdctdn")
    @Mapping(target = "metadata.zone", source = "zone")
    @Mapping(target = "metadata.passengerLoad", source = "psgld", qualifiedByName = "mapPassengerLoad")
    @Mapping(target = "metadata.gtfsSequence", source = "gtfsseq")
    @Mapping(target = "metadata.nextBus", source = "nbus")
    @Mapping(target = "metadata.scheduledStartSeconds", source = "stst")
    @Mapping(target = "metadata.scheduledStartDate", source = "stsd")
    @Mapping(target = "metadata.flagStop", source = "flagstop", qualifiedByName = "mapFlagStop")
    Prediction toDomain(CtaPrediction prediction);
}
