package com.cta4j.bus.locale.internal.impl;

import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.locale.LocalesApi;
import com.cta4j.bus.locale.internal.wire.CtaLocale;
import com.cta4j.bus.locale.internal.mapper.SupportedLocaleMapper;
import com.cta4j.bus.locale.internal.wire.CtaLocaleBustimeResponse;
import com.cta4j.bus.locale.internal.wire.CtaLocaleError;
import com.cta4j.bus.locale.model.SupportedLocale;
import com.cta4j.exception.Cta4jException;
import com.cta4j.common.internal.http.HttpClient;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class LocalesApiImpl implements LocalesApi {
    private static final Logger log = LoggerFactory.getLogger(LocalesApiImpl.class);

    private static final String LOCALES_ENDPOINT = "%s/getlocalelist".formatted(ApiUtils.API_PREFIX);
    private static final TypeReference<CtaResponse<CtaLocaleBustimeResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    private final BusApiConfig config;

    public LocalesApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<SupportedLocale> list() {
        String uri = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(LOCALES_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(uri);
    }

    @Override
    public List<SupportedLocale> list(Locale displayLocale) {
        Objects.requireNonNull(displayLocale);

        String languageTag = displayLocale.toLanguageTag();

        String uri = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(LOCALES_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .addParameter("locale", languageTag)
            .toString();

        return this.makeRequest(uri);
    }

    @Override
    public List<SupportedLocale> listInNativeLanguage() {
        String uri = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(LOCALES_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .addParameter("inLocaleLanguage", "true")
            .toString();

        return this.makeRequest(uri);
    }

    private List<SupportedLocale> makeRequest(String url) {
        String response = HttpClient.get(url);

        CtaResponse<CtaLocaleBustimeResponse> localeResponse;

        try {
            localeResponse = JsonMapper.shared()
                                       .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(LOCALES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaLocaleBustimeResponse bustimeResponse = localeResponse.bustimeResponse();

        List<CtaLocale> locales = bustimeResponse.locale();
        List<CtaLocaleError> errors = bustimeResponse.error();

        if (locales != null && !locales.isEmpty()) {
            return locales.stream()
                             .map(SupportedLocaleMapper.INSTANCE::toDomain)
                             .toList();
        }

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", LOCALES_ENDPOINT);

            return List.of();
        }

        String message = ApiUtils.buildErrorMessage(LOCALES_ENDPOINT, errors);

        throw new Cta4jException(message);
    }
}
