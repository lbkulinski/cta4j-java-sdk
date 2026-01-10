package com.cta4j.bus.mapper;

import com.cta4j.bus.external.detour.CtaDetour;
import com.cta4j.bus.model.Detour;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@ApiStatus.Internal
public interface DetourMapper {
    @Mapping(source = "ver", target = "version")
    @Mapping(source = "st", target = "active", qualifiedByName = "stToActive")
    @Mapping(source = "desc", target = "description")
    @Mapping(source = "rtdirs", target = "routeDirections")
    @Mapping(source = "startdt", target = "startTime", qualifiedByName = "toInstant")
    @Mapping(source = "enddt", target = "endTime", qualifiedByName = "toInstant")
    @Mapping(source = "rtpidatafeed", target = "dataFeed")
    Detour toDomain(CtaDetour dto);
}
