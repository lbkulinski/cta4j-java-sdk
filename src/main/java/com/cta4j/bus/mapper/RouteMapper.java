package com.cta4j.bus.mapper;

import com.cta4j.bus.external.route.CtaRoute;
import com.cta4j.bus.model.Route;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@ApiStatus.Internal
public interface RouteMapper {
    @Mapping(source = "rt", target = "id")
    @Mapping(source = "rtnm", target = "name")
    @Mapping(source = "rtdd", target = "description")
    @Mapping(source = "rtclr", target = "color")
    Route toDomain(CtaRoute route);
}
