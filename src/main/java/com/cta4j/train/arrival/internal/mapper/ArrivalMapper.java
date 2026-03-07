package com.cta4j.train.arrival.internal.mapper;

import com.cta4j.train.arrival.internal.wire.CtaArrival;
import com.cta4j.train.arrival.model.Arrival;
import com.cta4j.train.internal.mapper.Qualifiers;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface ArrivalMapper {
    ArrivalMapper INSTANCE = Mappers.getMapper(ArrivalMapper.class);

    @Mapping(target = "stationId", source = "staId")
    @Mapping(target = "stationName", source = "staNm")
    @Mapping(target = "stopId", source = "stpId")
    @Mapping(target = "stopDescription", source = "stpDe")
    @Mapping(target = "line", source = "rt")
    @Mapping(target = "destinationStationId", source = "destSt")
    @Mapping(target = "destinationName", source = "destNm")
    @Mapping(target = "predictionTime", source = "prdt", qualifiedByName = "mapTimestamp")
    @Mapping(target = "arrivalTime", source = "arrT", qualifiedByName = "mapTimestamp")
    @Mapping(target = "approaching", source = "isApp", qualifiedByName = "map01ToBoolean")
    @Mapping(target = "scheduled", source = "isSch", qualifiedByName = "map01ToBoolean")
    @Mapping(target = "delayed", source = "isDly", qualifiedByName = "map01ToBoolean")
    @Mapping(target = "fault", source = "isFlt", qualifiedByName = "map01ToBoolean")
    @Mapping(target = "metadata.runNumber", source = "rn")
    @Mapping(target = "metadata.direction", source = "trDr", qualifiedByName = "mapDirection")
    @Mapping(target = "metadata.latitude", source = "lat")
    @Mapping(target = "metadata.longitude", source = "lon")
    @Mapping(target = "metadata.heading", source = "heading")
    @Mapping(target = "metadata.flags", source = "flags")
    Arrival toDomain(CtaArrival arrival);
}
