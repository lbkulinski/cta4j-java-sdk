package com.cta4j.bus.locale.model;

import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Objects;

/// Represents a locale supported by the CTA Bus Tracker API.
///
/// @param locale the supported [Locale]
/// @param displayName the human-readable name of this supported locale (e.g., "English", "Spanish")
@NullMarked
public record SupportedLocale(
    Locale locale,
    String displayName
) {
    /// Constructs a `SupportedLocale`.
    ///
    /// @param locale the supported [Locale]
    /// @param displayName the human-readable name of the supported locale (e.g., "English", "Spanish")
    /// @throws NullPointerException if `locale` or `displayName` is `null`
    public SupportedLocale {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(displayName);
    }
}
