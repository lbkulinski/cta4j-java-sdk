package com.cta4j.bus.detour.internal.impl;

import com.cta4j.bus.common.BusApiConstants;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.detour.DetoursApi;
import com.cta4j.bus.detour.internal.wire.CtaDetour;
import com.cta4j.bus.detour.internal.mapper.DetourMapper;
import com.cta4j.bus.detour.internal.wire.CtaDetourBustimeResponse;
import com.cta4j.bus.detour.internal.wire.CtaDetourError;
import com.cta4j.bus.detour.model.Detour;
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
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class DetoursApiImpl implements DetoursApi {
    private static final Logger log = LoggerFactory.getLogger(DetoursApiImpl.class);

    private static final TypeReference<CtaResponse<CtaDetourBustimeResponse>> TYPE_REFERENCE =
        new TypeReference<>() {};

    private final BusApiConfig config;

    public DetoursApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Detour> list() {
        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.DETOURS_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Detour> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.DETOURS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Detour> findByRouteIdAndDirection(String routeId, String direction) {
        Objects.requireNonNull(routeId);
        Objects.requireNonNull(direction);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.DETOURS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("rtdir", direction)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<Detour> makeRequest(String url) {
        String response = HttpClient.get(url);

        CtaResponse<CtaDetourBustimeResponse> detoursResponse;

        try {
            detoursResponse = JsonMapper.shared()
                                        .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jBusException("Failed to parse response", BusApiConstants.DETOURS_ENDPOINT, e);
        }

        CtaDetourBustimeResponse bustimeResponse = detoursResponse.bustimeResponse();

        List<CtaDetour> detours = bustimeResponse.dtrs();
        List<CtaDetourError> errors = bustimeResponse.error();

        if (detours != null && !detours.isEmpty()) {
            return detours.stream()
                          .map(DetourMapper.INSTANCE::toDomain)
                          .toList();
        }

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", BusApiConstants.DETOURS_ENDPOINT);

            return List.of();
        }

        boolean notFound = errors.stream()
                                 .allMatch(error -> error.rt() != null);

        if (notFound) {
            return List.of();
        }

        throw new Cta4jBusException(errors, BusApiConstants.DETOURS_ENDPOINT);
    }
}
