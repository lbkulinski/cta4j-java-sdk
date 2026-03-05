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

    @Mapping(target = "id", source = "stpid")
    @Mapping(target = "name", source = "stpnm")
    @Mapping(target = "latitude", source = "lat")
    @Mapping(target = "longitude", source = "lon")
    @Mapping(target = "detoursAdded", source = "dtradd")
    @Mapping(target = "detoursRemoved", source = "dtrrem")
    @Mapping(target = "gtfsSequence", source = "gtfsseq")
    @Mapping(target = "adaAccessible", source = "ada")
    Stop toDomain(CtaStop stop);
}
