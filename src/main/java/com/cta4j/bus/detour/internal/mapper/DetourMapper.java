package com.cta4j.bus.detour.internal.mapper;

import com.cta4j.bus.detour.internal.wire.CtaDetour;
import com.cta4j.bus.detour.internal.wire.CtaDetoursRouteDirection;
import com.cta4j.bus.internal.mapper.Qualifiers;
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

    @Mapping(source = "ver", target = "version")
    @Mapping(source = "st", target = "active", qualifiedByName = "mapActive")
    @Mapping(source = "desc", target = "description")
    @Mapping(source = "rtdirs", target = "routeDirections")
    @Mapping(source = "startdt", target = "startTime", qualifiedByName = "mapTimestamp")
    @Mapping(source = "enddt", target = "endTime", qualifiedByName = "mapTimestamp")
    @Mapping(source = "rtpidatafeed", target = "dataFeed")
    Detour toDomain(CtaDetour dto);

    @Mapping(source = "rt", target = "route")
    @Mapping(source = "dir", target = "direction")
    DetourRouteDirection toDomain(CtaDetoursRouteDirection dto);
}
