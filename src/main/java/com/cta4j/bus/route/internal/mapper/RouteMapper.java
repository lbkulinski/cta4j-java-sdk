package com.cta4j.bus.route.internal.mapper;

import com.cta4j.bus.route.internal.wire.CtaRoute;
import com.cta4j.bus.route.model.Route;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
@ApiStatus.Internal
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    @Mapping(target = "id", source = "rt")
    @Mapping(target = "name", source = "rtnm")
    @Mapping(target = "color", source = "rtclr")
    @Mapping(target = "designator", source = "rtdd")
    @Mapping(target = "dataFeed", source = "rtpidatafeed")
    Route toDomain(CtaRoute route);
}
