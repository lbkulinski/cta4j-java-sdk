package com.cta4j.train.location.internal.mapper;

import com.cta4j.train.common.internal.mapper.Qualifiers;
import com.cta4j.train.location.internal.wire.CtaRoute;
import com.cta4j.train.location.model.TrainLocations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {Qualifiers.class, LocationTrainMapper.class})
public interface TrainLocationsMapper {
    @Mapping(target = "line", source = "name", qualifiedByName = "mapLine")
    @Mapping(target = "trains", source = "train")
    TrainLocations toDomain(CtaRoute route);
}
