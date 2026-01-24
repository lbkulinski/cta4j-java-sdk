package com.cta4j.bus.stop.internal.mapper;

import com.cta4j.bus.stop.internal.wire.CtaStop;
import com.cta4j.bus.stop.model.Stop;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
@ApiStatus.Internal
public interface StopMapper {
    StopMapper INSTANCE = Mappers.getMapper(StopMapper.class);

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
