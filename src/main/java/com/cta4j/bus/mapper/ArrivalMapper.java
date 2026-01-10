package com.cta4j.bus.mapper;

import com.cta4j.bus.external.prediction.CtaPredictionsPrd;
import com.cta4j.bus.model.Arrival;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@ApiStatus.Internal
public interface ArrivalMapper {
    @Mapping(source = "typ", target = "predictionType")
    @Mapping(source = "stpid", target = "stopId")
    @Mapping(source = "stpnm", target = "stopName")
    @Mapping(source = "vid", target = "vehicleId")
    @Mapping(source = "dstp", target = "distanceToStop")
    @Mapping(source = "rt", target = "route")
    @Mapping(source = "rtdd", target = "routeDesignator")
    @Mapping(source = "rtdir", target = "routeDirection")
    @Mapping(source = "des", target = "destination")
    @Mapping(source = "prdtm", target = "arrivalTime")
    @Mapping(source = "dly", target = "delayed")
    @Mapping(source = "tmstmp", target = "metadata.timestamp")
    @Mapping(source = "dyn", target = "metadata.dynamicAction")
    @Mapping(source = "tablockid", target = "metadata.blockId")
    @Mapping(source = "tatripid", target = "metadata.tripId")
    @Mapping(source = "origtatripno", target = "metadata.originalTripNumber")
    @Mapping(source = "zone", target = "metadata.zone")
    @Mapping(source = "psgld", target = "metadata.passengerLoad")
    @Mapping(source = "gtfsseq", target = "metadata.gtfsSequence")
    @Mapping(source = "nbus", target = "metadata.nextBus")
    @Mapping(source = "stst", target = "metadata.stopStatus")
    @Mapping(source = "stsd", target = "metadata.stopStatusDescription")
    @Mapping(source = "flagstop", target = "metadata.flagStop")
    Arrival toDomain(CtaPredictionsPrd prediction);
}
