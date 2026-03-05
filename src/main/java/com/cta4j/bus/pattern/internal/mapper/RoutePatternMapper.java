package com.cta4j.bus.pattern.internal.mapper;

import com.cta4j.bus.pattern.internal.wire.CtaPattern;
import com.cta4j.bus.pattern.internal.wire.CtaPoint;
import com.cta4j.bus.internal.mapper.Qualifiers;
import com.cta4j.bus.pattern.model.PatternPoint;
import com.cta4j.bus.pattern.model.RoutePattern;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface RoutePatternMapper {
    RoutePatternMapper INSTANCE = Mappers.getMapper(RoutePatternMapper.class);

    @Mapping(target = "id", source = "pid")
    @Mapping(target = "length", source = "ln")
    @Mapping(target = "direction", source = "rtdir")
    @Mapping(target = "points", source = "pt")
    @Mapping(target = "detourId", source = "dtrid")
    @Mapping(target = "detourPoints", source = "dtrpt")
    RoutePattern toDomain(CtaPattern pattern);

    @Mapping(target = "sequence", source = "seq")
    @Mapping(target = "type", source = "typ", qualifiedByName = "mapPatternPointType")
    @Mapping(target = "stopId", source = "stpid")
    @Mapping(target = "stopName", source = "stpnm")
    @Mapping(target = "distanceToPatternPoint", source = "pdist")
    @Mapping(target = "latitude", source = "lat")
    @Mapping(target = "longitude", source = "lon")
    PatternPoint toDomain(CtaPoint point);
}
