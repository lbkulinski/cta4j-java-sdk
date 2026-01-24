package com.cta4j.bus.api.locale;

import com.cta4j.bus.api.locale.model.SupportedLocale;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Locale;

@NullMarked
public interface LocalesApi {
    List<SupportedLocale> getLocales();

    List<SupportedLocale> getLocales(Locale displayLocale);

    List<SupportedLocale> getLocalesInNativeLanguage();
}
