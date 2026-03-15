package com.cta4j.bus.locale;

import com.cta4j.bus.locale.model.SupportedLocale;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Locale;

/**
 * Provides access to locale-related endpoints of the CTA BusTime API.
 * <p>
 * This API allows retrieval of supported locales for the CTA BusTime services.
 */
@NullMarked
public interface LocalesApi {
    /**
     * Retrieves the supported locales.
     *
     * @return a {@link List} of {@link SupportedLocale}s, or an empty {@link List} if no supported locales are
     * available
     */
    List<SupportedLocale> list();

    /**
     * Retrieves the supported locales, with names displayed in the specified locale.
     *
     * @param displayLocale the locale in which to display the names of the supported locales
     * @return a {@link List} of {@link SupportedLocale}s with names in the specified locale, or an empty {@link List}
     * if no supported locales are available
     */
    List<SupportedLocale> list(Locale displayLocale);

    /**
     * Retrieves the supported locales, with names displayed in their native languages.
     *
     * @return a {@link List} of {@link SupportedLocale}s with names in their native languages, or an empty
     * {@link List} if no supported locales are available
     */
    List<SupportedLocale> listInNativeLanguage();
}
