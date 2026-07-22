package com.cta4j.alert.routestatus.internal.mapper;

import com.cta4j.alert.common.internal.mapper.Qualifiers;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteInfo;
import com.cta4j.alert.routestatus.model.RouteStatus;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface RouteStatusMapper {
    RouteStatusMapper INSTANCE = Mappers.getMapper(RouteStatusMapper.class);

    @Mapping(target = "color", source = "routeColorCode")
    @Mapping(target = "textColor", source = "routeTextColor")
    @Mapping(target = "url", source = "routeUrl.cdataSection", qualifiedByName = "mapUri")
    @Mapping(target = "status", source = "routeStatus")
    @Mapping(target = "statusColor", source = "routeStatusColor")
    RouteStatus toDomain(CtaRouteInfo routeInfo);
}
