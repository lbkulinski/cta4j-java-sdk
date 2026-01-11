package com.cta4j.bus.mapper;

import com.cta4j.bus.external.CtaVehicle;
import com.cta4j.bus.mapper.util.CtaBusMappingQualifiers;
import com.cta4j.bus.model.Bus;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApiStatus.Internal
@Mapper(uses = CtaBusMappingQualifiers.class)
public interface BusMapper {
    @Mapping(source = "vid", target = "id")
    @Mapping(source = "rtpidatafeed", target = "metadata.dataFeed")
    @Mapping(source = "tmpstmp", target = "metadata.lastUpdated", qualifiedByName = "mapTimestamp")
    @Mapping(source = "lat", target = "coordinates.latitude")
    @Mapping(source = "lon", target = "coordinates.longitude")
    @Mapping(source = "hdg", target = "coordinates.heading")
    @Mapping(source = "pid", target = "metadata.patternId")
    @Mapping(source = "rt", target = "route")
    @Mapping(source = "des", target = "destination")
    @Mapping(source = "pdist", target = "metadata.distanceToPatternPoint")
    @Mapping(source = "stopstatus", target = "metadata.stopStatus")
    @Mapping(source = "timepointid", target = "metadata.timepointId")
    @Mapping(source = "stopid", target = "metadata.stopId")
    @Mapping(source = "sequence", target = "metadata.sequence")
    @Mapping(source = "gtfsseq", target = "metadata.gtfsSequence")
    @Mapping(source = "dly", target = "delayed")
    @Mapping(source = "srvtmstmp", target = "metadata.serverTimestamp", qualifiedByName = "mapTimestamp")
    @Mapping(source = "spd", target = "metadata.speed")
    @Mapping(source = "blk", target = "metadata.block")
    @Mapping(source = "tablockid", target = "metadata.blockId")
    @Mapping(source = "tatripid", target = "metadata.tripId")
    @Mapping(source = "origtatripno", target = "metadata.originalTripNumber")
    @Mapping(source = "zone", target = "metadata.zone")
    @Mapping(source = "mode", target = "metadata.mode", qualifiedByName = "mapTransitMode")
    @Mapping(source = "psgld", target = "metadata.passengerLoad", qualifiedByName = "mapPassengerLoad")
    @Mapping(source = "stst", target = "metadata.scheduledStartSeconds")
    @Mapping(source = "stsd", target = "metadata.scheduledStartDate")
    Bus toDomain(CtaVehicle vehicle);
}
