package com.cta4j.bus.api.detour.impl;

import com.cta4j.bus.api.core.context.BusApiContext;
import com.cta4j.bus.api.core.external.CtaBustimeResponse;
import com.cta4j.bus.api.core.external.CtaError;
import com.cta4j.bus.api.core.external.CtaResponse;
import com.cta4j.bus.api.core.util.ApiUtils;
import com.cta4j.bus.api.detour.DetoursApi;
import com.cta4j.bus.api.detour.external.CtaDetour;
import com.cta4j.bus.api.detour.mapper.DetourMapper;
import com.cta4j.bus.api.detour.model.Detour;
import com.cta4j.common.exception.Cta4jException;
import com.cta4j.common.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class DetoursApiImpl implements DetoursApi {
    private static final String DETOURS_ENDPOINT = String.format("%s/getdetours", ApiUtils.API_PREFIX);

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
        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaDetour>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaDetour>> detoursResponse;

        try {
            detoursResponse = this.context.objectMapper()
                                          .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", DETOURS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaDetour>> bustimeResponse = detoursResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaDetour> detours = bustimeResponse.data();

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(DETOURS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((detours == null) || detours.isEmpty()) {
            return List.of();
        }

        return detours.stream()
                      .map(DetourMapper.INSTANCE::toDomain)
                      .toList();
    }
}
