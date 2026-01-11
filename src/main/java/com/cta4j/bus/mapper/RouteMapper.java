package com.cta4j.bus.mapper;

import com.cta4j.bus.external.CtaRoute;
import com.cta4j.bus.model.Route;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApiStatus.Internal
@Mapper
public interface RouteMapper {
    @Mapping(source = "rt", target = "id")
    @Mapping(source = "rtnm", target = "name")
    @Mapping(source = "rtclr", target = "color")
    @Mapping(source = "rtdd", target = "designator")
    @Mapping(source = "rtpidatafeed", target = "dataFeed")
    Route toDomain(CtaRoute route);
}
