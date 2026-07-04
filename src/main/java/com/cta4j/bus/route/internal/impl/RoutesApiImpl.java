package com.cta4j.bus.route.internal.impl;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.route.RoutesApi;
import com.cta4j.bus.route.internal.mapper.RouteMapper;
import com.cta4j.bus.route.internal.wire.CtaRoute;
import com.cta4j.bus.route.internal.wire.CtaRouteBustimeResponse;
import com.cta4j.bus.route.internal.wire.CtaRouteError;
import com.cta4j.bus.route.model.Route;
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
public final class RoutesApiImpl implements RoutesApi {
    private static final Logger log = LoggerFactory.getLogger(RoutesApiImpl.class);

    private static final TypeReference<CtaResponse<CtaRouteBustimeResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    private final BusApiConfig config;

    public RoutesApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Route> list() {
        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.ROUTES_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        String response = HttpClient.get(url);

        CtaResponse<CtaRouteBustimeResponse> routesResponse;

        try {
            routesResponse = JsonMapper.shared()
                                       .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response";

            throw new Cta4jBusException(message, BusApiConstants.ROUTES_ENDPOINT, e);
        }

        CtaRouteBustimeResponse bustimeResponse = routesResponse.bustimeResponse();

        List<CtaRoute> routes = bustimeResponse.routes();
        List<CtaRouteError> errors = bustimeResponse.error();

        if (routes != null && !routes.isEmpty()) {
            return routes.stream()
                         .map(RouteMapper.INSTANCE::toDomain)
                         .toList();
        }

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", BusApiConstants.ROUTES_ENDPOINT);

            return List.of();
        }

        throw new Cta4jBusException(errors, BusApiConstants.ROUTES_ENDPOINT);
    }
}
