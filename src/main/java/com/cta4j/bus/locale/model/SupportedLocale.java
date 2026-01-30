package com.cta4j.bus.locale.model;

import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents a locale supported by the CTA Bus API.
 *
 * @param locale the supported {@link Locale}
 * @param displayName the human-readable name of this locale
 */
@NullMarked
public record SupportedLocale(
    Locale locale,

    String displayName
) {
    /**
     * Creates a {@code SupportedLocale}.
     *
     * @param locale the supported {@link Locale}
     * @param displayName the human-readable name of the locale
     * @throws NullPointerException if {@code locale} or {@code displayName} is {@code null}
     */
    public SupportedLocale {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(displayName);
    }
}
