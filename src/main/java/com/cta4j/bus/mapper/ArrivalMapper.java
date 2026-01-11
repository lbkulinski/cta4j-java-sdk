package com.cta4j.bus.mapper;

import com.cta4j.bus.external.CtaPrediction;
import com.cta4j.bus.mapper.util.CtaBusMappingQualifiers;
import com.cta4j.bus.model.Arrival;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApiStatus.Internal
@Mapper(uses = CtaBusMappingQualifiers.class)
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
    @Mapping(source = "stst", target = "metadata.scheduledStartSeconds")
    @Mapping(source = "stsd", target = "metadata.scheduledStartDate")
    @Mapping(source = "flagstop", target = "metadata.flagStop", qualifiedByName = "mapFlagStop")
    Arrival toDomain(CtaPrediction prediction);
}
