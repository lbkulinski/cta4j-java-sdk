package com.cta4j.train.station.internal.mapper;

import com.cta4j.train.internal.mapper.Qualifiers;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.Station;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface StationMapper {
    StationMapper INSTANCE = Mappers.getMapper(StationMapper.class);

    @Mapping(target = "stopId", source = "stopId")
    @Mapping(target = "direction", source = "directionId", qualifiedByName = "mapDirection")
    @Mapping(target = "stopName", source = "stopName")
    @Mapping(target = "name", source = "stationName")
    @Mapping(target = "descriptiveName", source = "stationDescriptiveName")
    @Mapping(target = "mapId", source = "mapId")
    @Mapping(target = "adaAccessible", source = "ada")
    @Mapping(target = "lines", source = ".", qualifiedByName = "mapTrainLines")
    @Mapping(target = "location", source = "location", qualifiedByName = "mapLocation")
    Station toDomain(CtaStation station);
}
