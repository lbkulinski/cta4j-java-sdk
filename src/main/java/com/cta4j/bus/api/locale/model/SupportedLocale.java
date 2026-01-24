package com.cta4j.bus.api.locale.model;

import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Objects;

@NullMarked
public record SupportedLocale(
    Locale locale,

    String displayName
) {
    public SupportedLocale {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(displayName);
    }
}
