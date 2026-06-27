package com.cta4j.bus.route.internal.impl;

import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.route.RoutesApi;
import com.cta4j.bus.route.internal.wire.CtaRoute;
import com.cta4j.bus.route.internal.mapper.RouteMapper;
import com.cta4j.bus.route.internal.wire.CtaRouteBustimeResponse;
import com.cta4j.bus.route.internal.wire.CtaRouteError;
import com.cta4j.bus.route.model.Route;
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
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class RoutesApiImpl implements RoutesApi {
    private static final Logger log = LoggerFactory.getLogger(RoutesApiImpl.class);

    private static final String ROUTES_ENDPOINT = "%s/getroutes".formatted(ApiUtils.API_PREFIX);

    private final BusApiConfig config;

    public RoutesApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Route> list() {
        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.config.host())
            .setPath(ROUTES_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        String response = HttpClient.get(url);

        TypeReference<CtaResponse<CtaRouteBustimeResponse>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaRouteBustimeResponse> routesResponse;

        try {
            routesResponse = JsonMapper.shared()
                                       .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(ROUTES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaRouteBustimeResponse bustimeResponse = routesResponse.bustimeResponse();

        List<CtaRoute> routes = bustimeResponse.route();
        List<CtaRouteError> errors = bustimeResponse.error();

        if (routes != null && !routes.isEmpty()) {
            return routes.stream()
                         .map(RouteMapper.INSTANCE::toDomain)
                         .toList();
        }

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", ROUTES_ENDPOINT);

            return List.of();
        }

        String message = ApiUtils.buildErrorMessage(ROUTES_ENDPOINT, errors);

        throw new Cta4jException(message);
    }
}
