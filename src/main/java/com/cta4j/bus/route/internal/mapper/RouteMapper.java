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

    @Mapping(source = "rt", target = "id")
    @Mapping(source = "rtnm", target = "name")
    @Mapping(source = "rtclr", target = "color")
    @Mapping(source = "rtdd", target = "designator")
    @Mapping(source = "rtpidatafeed", target = "dataFeed")
    Route toDomain(CtaRoute route);
}
