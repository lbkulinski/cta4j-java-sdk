package com.cta4j.bus.locale.internal.impl;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.locale.LocalesApi;
import com.cta4j.bus.locale.internal.mapper.SupportedLocaleMapper;
import com.cta4j.bus.locale.internal.wire.CtaLocale;
import com.cta4j.bus.locale.internal.wire.CtaLocaleBustimeResponse;
import com.cta4j.bus.locale.internal.wire.CtaLocaleError;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.locale.model.SupportedLocale;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class LocalesApiImpl implements LocalesApi {
    private static final TypeReference<CtaResponse<CtaLocaleBustimeResponse>> TYPE_REFERENCE =
        new TypeReference<>() {};

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
            .setPath(BusApiConstants.LOCALES_ENDPOINT)
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
            .setPath(BusApiConstants.LOCALES_ENDPOINT)
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
            .setPath(BusApiConstants.LOCALES_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .addParameter("inLocaleLanguage", "true")
            .toString();

        return this.makeRequest(uri);
    }

    private List<SupportedLocale> makeRequest(String url) {
        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = e.getMessage();

            throw new Cta4jBusException(message, BusApiConstants.LOCALES_ENDPOINT, e);
        }

        CtaResponse<CtaLocaleBustimeResponse> localeResponse;

        try {
            localeResponse = JsonMapper.shared()
                                       .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jBusException("Failed to parse response", BusApiConstants.LOCALES_ENDPOINT, e);
        }

        CtaLocaleBustimeResponse bustimeResponse = localeResponse.bustimeResponse();

        List<CtaLocale> locales = bustimeResponse.locale();
        List<CtaLocaleError> errors = bustimeResponse.error();

        if (locales != null && !locales.isEmpty()) {
            return locales.stream()
                             .map(SupportedLocaleMapper.INSTANCE::toDomain)
                             .toList();
        }

        ApiUtils.checkErrors(errors, BusApiConstants.LOCALES_ENDPOINT);

        return List.of();
    }
}
