package com.cta4j.bus.mapper;

import com.cta4j.bus.external.CtaDetour;
import com.cta4j.bus.external.CtaDetoursRouteDirection;
import com.cta4j.bus.model.Detour;
import com.cta4j.bus.model.DetourRouteDirection;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
@ApiStatus.Internal
public interface DetourMapper {
    @Mapping(source = "ver", target = "version")
    @Mapping(source = "st", target = "active", qualifiedByName = "mapActive")
    @Mapping(source = "desc", target = "description")
    @Mapping(source = "rtdirs", target = "routeDirections")
    @Mapping(source = "startdt", target = "startTime")
    @Mapping(source = "enddt", target = "endTime")
    @Mapping(source = "rtpidatafeed", target = "dataFeed")
    Detour toDomain(CtaDetour dto);

    @Mapping(source = "rt", target = "route")
    @Mapping(source = "dir", target = "direction")
    DetourRouteDirection toDomain(CtaDetoursRouteDirection dto);

    @Named("mapActive")
    default boolean mapActive(int st) {
        return st == 1;
    }
}
