package com.cta4j.alert.routestatus.internal.mapper;

import com.cta4j.alert.common.internal.mapper.Qualifiers;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteInfo;
import com.cta4j.alert.routestatus.model.TrainRouteStatus;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface TrainRouteStatusMapper {
    TrainRouteStatusMapper INSTANCE = Mappers.getMapper(TrainRouteStatusMapper.class);

    @Mapping(target = "color", source = "routeColorCode")
    @Mapping(target = "textColor", source = "routeTextColor")
    @Mapping(target = "url", source = "routeUrl.cdataSection", qualifiedByName = "mapUri")
    @Mapping(target = "line", source = "serviceId", qualifiedByName = "mapTrainLine")
    @Mapping(target = "status", source = "routeStatus")
    @Mapping(target = "statusColor", source = "routeStatusColor")
    TrainRouteStatus toDomain(CtaRouteInfo routeInfo);
}
