package com.cta4j.alert.detailedalert.internal.mapper;

import com.cta4j.alert.common.internal.mapper.Qualifiers;
import com.cta4j.alert.detailedalert.internal.wire.CtaAlert;
import com.cta4j.alert.detailedalert.model.Alert;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface AlertMapper {
    AlertMapper INSTANCE = Mappers.getMapper(AlertMapper.class);

    @Mapping(target = "id", source = "alertId")
    @Mapping(target = "severity.score", source = "severityScore")
    @Mapping(target = "severity.color", source = "severityColor")
    @Mapping(target = "severity.css", source = "severityCss")
    @Mapping(target = "startTime", source = "eventStart", qualifiedByName = "mapTimestamp")
    @Mapping(target = "endTime", source = "eventEnd", qualifiedByName = "mapTimestamp")
    @Mapping(target = "openEnded", source = "tbd", qualifiedByName = "map01ToBoolean")
    //todo: finish mapping
    Alert toDomain(CtaAlert alert);
}
