package com.cta4j.bus.locale;

import com.cta4j.bus.locale.internal.mapper.SupportedLocaleMapper;
import com.cta4j.bus.locale.internal.wire.CtaLocale;
import com.cta4j.bus.locale.model.SupportedLocale;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

class SupportedLocaleMapperTest {
    @Test
    void toDomain_mapsLocaleStringAndDisplayName() {
        CtaLocale wire = new CtaLocale("en", "English");

        SupportedLocale locale = SupportedLocaleMapper.INSTANCE.toDomain(wire);

        assertThat(locale.locale()).isEqualTo(Locale.of("en"));
        assertThat(locale.displayName()).isEqualTo("English");
    }

    @Test
    void toDomain_mapsSpanishLocale() {
        CtaLocale wire = new CtaLocale("es", "Spanish");

        SupportedLocale locale = SupportedLocaleMapper.INSTANCE.toDomain(wire);

        assertThat(locale.locale()).isEqualTo(Locale.of("es"));
        assertThat(locale.displayName()).isEqualTo("Spanish");
    }
}
