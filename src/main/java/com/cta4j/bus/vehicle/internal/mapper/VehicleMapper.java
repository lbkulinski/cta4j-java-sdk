package com.cta4j.bus.vehicle.internal.mapper;

import com.cta4j.bus.vehicle.internal.wire.CtaVehicle;
import com.cta4j.bus.internal.mapper.Qualifiers;
import com.cta4j.bus.vehicle.model.Vehicle;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface VehicleMapper {
    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

    @Mapping(target = "id", source = "vid")
    @Mapping(target = "metadata.dataFeed", source = "rtpidatafeed")
    @Mapping(target = "metadata.lastUpdated", source = "tmpstmp", qualifiedByName = "mapTimestamp")
    @Mapping(target = "coordinates.latitude", source = "lat")
    @Mapping(target = "coordinates.longitude", source = "lon")
    @Mapping(target = "coordinates.heading", source = "hdg")
    @Mapping(target = "metadata.patternId", source = "pid")
    @Mapping(target = "route", source = "rt")
    @Mapping(target = "destination", source = "des")
    @Mapping(target = "metadata.distanceToPatternPoint", source = "pdist")
    @Mapping(target = "metadata.stopStatus", source = "stopstatus")
    @Mapping(target = "metadata.timepointId", source = "timepointid")
    @Mapping(target = "metadata.stopId", source = "stopid")
    @Mapping(target = "metadata.sequence", source = "sequence")
    @Mapping(target = "metadata.gtfsSequence", source = "gtfsseq")
    @Mapping(target = "delayed", source = "dly")
    @Mapping(target = "metadata.serverTimestamp", source = "srvtmstmp", qualifiedByName = "mapTimestamp")
    @Mapping(target = "metadata.speed", source = "spd")
    @Mapping(target = "metadata.block", source = "blk")
    @Mapping(target = "metadata.blockId", source = "tablockid")
    @Mapping(target = "metadata.tripId", source = "tatripid")
    @Mapping(target = "metadata.originalTripNumber", source = "origtatripno")
    @Mapping(target = "metadata.zone", source = "zone")
    @Mapping(target = "metadata.mode", source = "mode", qualifiedByName = "mapTransitMode")
    @Mapping(target = "metadata.passengerLoad", source = "psgld", qualifiedByName = "mapPassengerLoad")
    @Mapping(target = "metadata.scheduledStartSeconds", source = "stst")
    @Mapping(target = "metadata.scheduledStartDate", source = "stsd")
    Vehicle toDomain(CtaVehicle vehicle);
}
