package com.cta4j.bus.api.stop.mapper;

import com.cta4j.bus.api.stop.external.CtaStop;
import com.cta4j.bus.api.stop.model.Stop;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@ApiStatus.Internal
public interface StopMapper {
    @Mapping(source = "stpid", target = "id")
    @Mapping(source = "stpnm", target = "name")
    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    @Mapping(source = "dtradd", target = "detoursAdded")
    @Mapping(source = "dtrrem", target = "detoursRemoved")
    @Mapping(source = "gtfsseq", target = "gtfsSequence")
    @Mapping(source = "ada", target = "adaAccessible")
    Stop toDomain(CtaStop stop);
}
