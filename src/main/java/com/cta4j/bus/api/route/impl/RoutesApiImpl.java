package com.cta4j.bus.api.route.impl;

import com.cta4j.bus.api.common.util.ApiUtils;
import com.cta4j.bus.api.route.RoutesApi;
import com.cta4j.bus.api.route.external.CtaRoute;
import com.cta4j.bus.api.route.mapper.RouteMapper;
import com.cta4j.bus.api.route.model.Route;
import com.cta4j.bus.api.common.external.CtaBustimeResponse;
import com.cta4j.bus.api.common.external.CtaError;
import com.cta4j.bus.api.common.external.CtaResponse;
import com.cta4j.exception.Cta4jException;
import com.cta4j.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class RoutesApiImpl implements RoutesApi {
    private static final Logger log = LoggerFactory.getLogger(RoutesApiImpl.class);

    private static final String ROUTES_ENDPOINT = String.format("%s/getroutes", ApiUtils.API_PREFIX);

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public RoutesApiImpl(
        String host,
        String apiKey,
        ObjectMapper objectMapper
    ) {
        this.host = Objects.requireNonNull(host);
        this.apiKey = Objects.requireNonNull(apiKey);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public List<Route> list() {
        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(ROUTES_ENDPOINT)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaRoute>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaRoute>> routesResponse;

        try {
            routesResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", ROUTES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaRoute>> bustimeResponse = routesResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaRoute> routes = bustimeResponse.data();

        if ((errors == null) && (routes == null)) {
            log.debug("Routes bustime response missing both error and data from {}", ROUTES_ENDPOINT);

            return List.of();
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(ROUTES_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((routes == null) || routes.isEmpty()) {
            return List.of();
        }

        return routes.stream()
                     .map(RouteMapper.MAPPER::toDomain)
                     .toList();
    }
}
