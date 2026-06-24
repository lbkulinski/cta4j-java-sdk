package com.cta4j.bus.detour.internal.impl;

import com.cta4j.bus.common.internal.context.BusApiContext;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.detour.DetoursApi;
import com.cta4j.bus.detour.internal.wire.CtaDetour;
import com.cta4j.bus.detour.internal.mapper.DetourMapper;
import com.cta4j.bus.detour.internal.wire.CtaDetourBustimeResponse;
import com.cta4j.bus.detour.internal.wire.CtaDetourError;
import com.cta4j.bus.detour.model.Detour;
import com.cta4j.exception.Cta4jException;
import com.cta4j.common.internal.http.HttpClient;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class DetoursApiImpl implements DetoursApi {
    private static final Logger log = LoggerFactory.getLogger(DetoursApiImpl.class);

    private static final String DETOURS_ENDPOINT = "%s/getdetours".formatted(ApiUtils.API_PREFIX);

    private final BusApiContext context;

    public DetoursApiImpl(BusApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<Detour> list() {
        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(DETOURS_ENDPOINT)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Detour> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(DETOURS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Detour> findByRouteIdAndDirection(String routeId, String direction) {
        Objects.requireNonNull(routeId);
        Objects.requireNonNull(direction);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(DETOURS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("rtdir", direction)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<Detour> makeRequest(String url) {
        String response = HttpClient.get(url);

        TypeReference<CtaResponse<CtaDetourBustimeResponse>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaDetourBustimeResponse> detoursResponse;

        try {
            detoursResponse = this.context.jsonMapper()
                                          .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(DETOURS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaDetourBustimeResponse bustimeResponse = detoursResponse.bustimeResponse();

        List<CtaDetour> detours = bustimeResponse.dtr();
        List<CtaDetourError> errors = bustimeResponse.error();

        if (detours != null && !detours.isEmpty()) {
            return detours.stream()
                          .map(DetourMapper.INSTANCE::toDomain)
                          .toList();
        }

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", DETOURS_ENDPOINT);

            return List.of();
        }

        boolean notFound = errors.stream()
                                 .allMatch(error -> error.rt() != null || error.rtdir() != null);

        if (notFound) {
            return List.of();
        }

        String message = ApiUtils.buildErrorMessage(DETOURS_ENDPOINT, errors);

        throw new Cta4jException(message);
    }
}
