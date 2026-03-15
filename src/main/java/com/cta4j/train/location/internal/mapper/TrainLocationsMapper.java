package com.cta4j.train.location.internal.mapper;

import com.cta4j.train.common.internal.mapper.Qualifiers;
import com.cta4j.train.location.internal.wire.CtaRoute;
import com.cta4j.train.location.model.TrainLocations;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {Qualifiers.class, LocationTrainMapper.class},
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
@ApiStatus.Internal
public interface TrainLocationsMapper {
    TrainLocationsMapper INSTANCE = Mappers.getMapper(TrainLocationsMapper.class);

    @Mapping(target = "line", source = "name", qualifiedByName = "mapLine")
    @Mapping(target = "trains", source = "train")
    TrainLocations toDomain(CtaRoute route);
}
