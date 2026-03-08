package com.cta4j.bus.detour.internal.mapper;

import com.cta4j.bus.detour.internal.wire.CtaDetour;
import com.cta4j.bus.detour.internal.wire.CtaDetoursRouteDirection;
import com.cta4j.bus.common.internal.mapper.Qualifiers;
import com.cta4j.bus.detour.model.Detour;
import com.cta4j.bus.detour.model.DetourRouteDirection;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface DetourMapper {
    DetourMapper INSTANCE = Mappers.getMapper(DetourMapper.class);

    @Mapping(target = "version", source = "ver")
    @Mapping(target = "active", source = "st", qualifiedByName = "mapActive")
    @Mapping(target = "description", source = "desc")
    @Mapping(target = "routeDirections", source = "rtdirs")
    @Mapping(target = "startTime", source = "startdt", qualifiedByName = "mapTimestamp")
    @Mapping(target = "endTime", source = "enddt", qualifiedByName = "mapTimestamp")
    @Mapping(target = "dataFeed", source = "rtpidatafeed")
    Detour toDomain(CtaDetour dto);

    @Mapping(target = "routeId", source = "rt")
    @Mapping(target = "direction", source = "dir")
    DetourRouteDirection toDomain(CtaDetoursRouteDirection dto);
}
