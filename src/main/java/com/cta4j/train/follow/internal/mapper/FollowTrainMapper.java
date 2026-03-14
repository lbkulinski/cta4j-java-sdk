package com.cta4j.train.follow.internal.mapper;

import com.cta4j.train.common.internal.mapper.ArrivalMapper;
import com.cta4j.train.common.internal.mapper.Qualifiers;
import com.cta4j.train.follow.internal.wire.CtaFollowResponse;
import com.cta4j.train.follow.model.FollowTrain;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {Qualifiers.class, ArrivalMapper.class}, nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
@ApiStatus.Internal
public interface FollowTrainMapper {
    FollowTrainMapper INSTANCE = Mappers.getMapper(FollowTrainMapper.class);

    @Mapping(target = "coordinates", source = "position", qualifiedByName = "mapCoordinates")
    @Mapping(target = "arrivals", source = "eta")
    FollowTrain toDomain(CtaFollowResponse response);
}
