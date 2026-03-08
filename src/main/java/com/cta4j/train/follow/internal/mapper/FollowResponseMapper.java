package com.cta4j.train.follow.internal.mapper;

import com.cta4j.train.arrival.internal.mapper.ArrivalMapper;
import com.cta4j.train.common.internal.mapper.Qualifiers;
import com.cta4j.train.follow.internal.wire.CtaFollowResponse;
import com.cta4j.train.follow.model.FollowResponse;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {Qualifiers.class, ArrivalMapper.class})
@ApiStatus.Internal
public interface FollowResponseMapper {
    FollowResponseMapper INSTANCE = Mappers.getMapper(FollowResponseMapper.class);

    @Mapping(target = "coordinates", source = "position", qualifiedByName = "mapCoordinates")
    @Mapping(target = "arrivals", source = "eta")
    FollowResponse toDomain(CtaFollowResponse response);
}
