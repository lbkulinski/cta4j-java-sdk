package com.cta4j.bus.locale.internal.mapper;

import com.cta4j.bus.internal.mapper.Qualifiers;
import com.cta4j.bus.locale.internal.wire.CtaLocale;
import com.cta4j.bus.locale.model.SupportedLocale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
public interface SupportedLocaleMapper {
    SupportedLocaleMapper INSTANCE = Mappers.getMapper(SupportedLocaleMapper.class);

    @Mapping(source = "localestring", target = "locale", qualifiedByName = "mapLocale")
    @Mapping(source = "displayname", target = "displayName")
    SupportedLocale toDomain(CtaLocale locale);
}
