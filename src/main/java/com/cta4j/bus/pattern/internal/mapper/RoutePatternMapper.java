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

    @Mapping(source = "pid", target = "patternId")
    @Mapping(source = "ln", target = "patternCount")
    @Mapping(source = "rtdir", target = "direction")
    @Mapping(source = "pt", target = "points")
    @Mapping(source = "dtrid", target = "detourId")
    @Mapping(source = "dtrpt", target = "detourPoints")
    RoutePattern toDomain(CtaPattern pattern);

    @Mapping(source = "seq", target = "sequence")
    @Mapping(source = "typ", target = "type", qualifiedByName = "mapPatternPointType")
    @Mapping(source = "stpid", target = "stopId")
    @Mapping(source = "stpnm", target = "stopName")
    @Mapping(source = "pdist", target = "distanceToPatternPoint")
    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    PatternPoint toDomain(CtaPoint point);
}
