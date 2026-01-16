package com.cta4j.bus.mapper;

import com.cta4j.bus.external.CtaPattern;
import com.cta4j.bus.external.CtaPoint;
import com.cta4j.bus.mapper.util.CtaBusMappingQualifiers;
import com.cta4j.bus.model.PatternPoint;
import com.cta4j.bus.model.RoutePattern;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApiStatus.Internal
@Mapper(uses = CtaBusMappingQualifiers.class)
public interface RoutePatternMapper {
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
