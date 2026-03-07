package com.cta4j.bus.locale.internal.impl;

import com.cta4j.bus.internal.context.BusApiContext;
import com.cta4j.bus.internal.wire.CtaBustimeResponse;
import com.cta4j.bus.internal.wire.CtaError;
import com.cta4j.bus.internal.wire.CtaResponse;
import com.cta4j.bus.internal.util.ApiUtils;
import com.cta4j.bus.locale.LocalesApi;
import com.cta4j.bus.locale.internal.wire.CtaLocale;
import com.cta4j.bus.locale.internal.mapper.SupportedLocaleMapper;
import com.cta4j.bus.locale.model.SupportedLocale;
import com.cta4j.exception.Cta4jException;
import com.cta4j.internal.http.HttpClient;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class LocalesApiImpl implements LocalesApi {
    private static final String LOCALES_ENDPOINT = String.format("%s/getlocalelist", ApiUtils.API_PREFIX);

    private final BusApiContext context;

    public LocalesApiImpl(BusApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<SupportedLocale> list() {
        String uri = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(LOCALES_ENDPOINT)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(uri);
    }

    @Override
    public List<SupportedLocale> list(Locale displayLocale) {
        Objects.requireNonNull(displayLocale);

        String languageTag = displayLocale.toLanguageTag();

        String uri = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(LOCALES_ENDPOINT)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .addParameter("locale", languageTag)
            .toString();

        return this.makeRequest(uri);
    }

    @Override
    public List<SupportedLocale> listInNativeLanguage() {
        String uri = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(LOCALES_ENDPOINT)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .addParameter("inLocaleLanguage", "true")
            .toString();

        return this.makeRequest(uri);
    }

    private List<SupportedLocale> makeRequest(String url) {
        String response = HttpClient.get(url);

        TypeReference<CtaResponse<List<CtaLocale>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaLocale>> localeResponse;

        try {
            localeResponse = this.context.objectMapper()
                                         .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", LOCALES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaLocale>> bustimeResponse = localeResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaLocale> locales = bustimeResponse.data();

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(LOCALES_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((locales == null) || locales.isEmpty()) {
            return List.of();
        }

        return locales.stream()
                      .map(SupportedLocaleMapper.INSTANCE::toDomain)
                      .toList();
    }
}
