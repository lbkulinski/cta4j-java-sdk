package com.cta4j.bus.mapper;

import com.cta4j.bus.external.vehicle.CtaVehicle;
import com.cta4j.bus.model.Bus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BusMapper {
    @Mapping(source = "vid", target = "id")
    @Mapping(source = "rt", target = "route")
    @Mapping(source = "des", target = "destination")
    @Mapping(source = "lat", target = "coordinates.latitude")
    @Mapping(source = "lon", target = "coordinates.longitude")
    @Mapping(source = "hdg", target = "coordinates.heading")
    @Mapping(source = "dly", target = "delayed")
    @Mapping(source = "rtpidatafeed", target = "metadata.dataFeed")
    @Mapping(source = "srvtmstmp", target = "metadata.serverTimestamp")
    @Mapping(source = "spd", target = "metadata.speed")
    @Mapping(source = "pid", target = "metadata.patternId")
    @Mapping(source = "pdist", target = "metadata.distanceToPatternPoint")
    @Mapping(source = "stopstatus", target = "metadata.stopStatus")
    @Mapping(source = "timepointid", target = "metadata.timepointId")
    @Mapping(source = "stopid", target = "metadata.stopId")
    @Mapping(source = "sequence", target = "metadata.sequence")
    @Mapping(source = "gtfsseq", target = "metadata.gtfsSequence")
    @Mapping(source = "blk", target = "metadata.block")
    @Mapping(source = "tablockid", target = "metadata.taBlockId")
    @Mapping(source = "tatripid", target = "metadata.taTripId")
    @Mapping(source = "origtatripno", target = "metadata.originalTaTripNo")
    @Mapping(source = "zone", target = "metadata.zone")
    @Mapping(source = "mode", target = "metadata.mode")
    @Mapping(source = "psgld", target = "metadata.passengerLoad")
    @Mapping(source = "stst", target = "metadata.scheduledStartSeconds")
    @Mapping(source = "stsd", target = "metadata.scheduledStartDate")
    Bus toDomain(CtaVehicle vehicle);
}
