package com.cta4j.bus.api.locale.mapper;

import com.cta4j.bus.api.core.util.CtaBusMappingQualifiers;
import com.cta4j.bus.api.locale.external.CtaLocale;
import com.cta4j.bus.api.locale.model.SupportedLocale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CtaBusMappingQualifiers.class)
public interface SupportedLocaleMapper {
    SupportedLocaleMapper INSTANCE = Mappers.getMapper(SupportedLocaleMapper.class);

    @Mapping(source = "localestring", target = "locale", qualifiedByName = "mapLocale")
    @Mapping(source = "displayname", target = "displayName")
    SupportedLocale toDomain(CtaLocale locale);
}
