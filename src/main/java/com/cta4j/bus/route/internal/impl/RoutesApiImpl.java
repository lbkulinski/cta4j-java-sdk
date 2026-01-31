package com.cta4j.bus.route.internal.impl;

import com.cta4j.bus.internal.context.BusApiContext;
import com.cta4j.bus.internal.util.ApiUtils;
import com.cta4j.bus.route.RoutesApi;
import com.cta4j.bus.route.internal.wire.CtaRoute;
import com.cta4j.bus.route.internal.mapper.RouteMapper;
import com.cta4j.bus.route.model.Route;
import com.cta4j.bus.internal.wire.CtaBustimeResponse;
import com.cta4j.bus.internal.wire.CtaError;
import com.cta4j.bus.internal.wire.CtaResponse;
import com.cta4j.exception.Cta4jException;
import com.cta4j.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class RoutesApiImpl implements RoutesApi {
    private static final Logger log = LoggerFactory.getLogger(RoutesApiImpl.class);

    private static final String ROUTES_ENDPOINT = String.format("%s/getroutes", ApiUtils.API_PREFIX);

    private final BusApiContext context;

    public RoutesApiImpl(BusApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<Route> list() {
        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(ROUTES_ENDPOINT)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaRoute>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaRoute>> routesResponse;

        try {
            routesResponse = this.context.objectMapper()
                                         .readValue(response, typeReference);
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
                     .map(RouteMapper.INSTANCE::toDomain)
                     .toList();
    }
}
