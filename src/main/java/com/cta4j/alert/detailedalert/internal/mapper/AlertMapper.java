package com.cta4j.alert.detailedalert.internal.mapper;

import com.cta4j.alert.common.internal.mapper.Qualifiers;
import com.cta4j.alert.detailedalert.internal.wire.CtaAlert;
import com.cta4j.alert.detailedalert.internal.wire.CtaImpactedService;
import com.cta4j.alert.detailedalert.model.Alert;
import com.cta4j.alert.detailedalert.model.ImpactedService;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface AlertMapper {
    AlertMapper INSTANCE = Mappers.getMapper(AlertMapper.class);

    @Mapping(target = "id", source = "alertId")
    @Mapping(target = "fullDescription", source = "fullDescription.cdataSection")
    @Mapping(target = "severity.score", source = "severityScore", qualifiedByName = "mapScore")
    @Mapping(target = "severity.color", source = "severityColor")
    @Mapping(target = "severity.css", source = "severityCss")
    @Mapping(target = "startTime", source = "eventStart", qualifiedByName = "mapTimestamp")
    @Mapping(target = "endTime", source = "eventEnd", qualifiedByName = "mapTimestamp")
    @Mapping(target = "openEnded", source = "tbd", qualifiedByName = "map01ToBoolean")
    @Mapping(target = "major", source = "majorAlert", qualifiedByName = "map01ToBoolean")
    @Mapping(target = "url", source = "alertUrl.cdataSection", qualifiedByName = "mapUri")
    @Mapping(target = "impactedServices", source = "impactedService.service")
    @Mapping(target = "ttim", source = "ttim")
    @Mapping(target = "guid", source = "guid")
    Alert toDomain(CtaAlert alert);

    @Mapping(target = "type", source = "serviceType", qualifiedByName = "mapServiceType")
    @Mapping(target = "typeDescription", source = "serviceTypeDescription")
    @Mapping(target = "name", source = "serviceName")
    @Mapping(target = "color", source = "serviceBackColor")
    @Mapping(target = "textColor", source = "serviceTextColor")
    @Mapping(target = "url", source = "serviceUrl.cdataSection", qualifiedByName = "mapUri")
    ImpactedService toDomain(CtaImpactedService impactedService);
}
