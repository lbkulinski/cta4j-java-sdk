package com.cta4j.bus.locale.internal.mapper;

import com.cta4j.bus.internal.mapper.Qualifiers;
import com.cta4j.bus.locale.internal.wire.CtaLocale;
import com.cta4j.bus.locale.model.SupportedLocale;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Qualifiers.class)
@ApiStatus.Internal
public interface SupportedLocaleMapper {
    SupportedLocaleMapper INSTANCE = Mappers.getMapper(SupportedLocaleMapper.class);

    @Mapping(target = "locale", source = "localestring", qualifiedByName = "mapLocale")
    @Mapping(target = "displayName", source = "displayname")
    SupportedLocale toDomain(CtaLocale locale);
}
