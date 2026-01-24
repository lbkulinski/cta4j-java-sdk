package com.cta4j.bus.api.direction.impl;

import com.cta4j.bus.api.core.context.BusApiContext;
import com.cta4j.bus.api.core.util.ApiUtils;
import com.cta4j.bus.api.direction.DirectionsApi;
import com.cta4j.bus.api.core.external.CtaBustimeResponse;
import com.cta4j.bus.api.direction.external.CtaDirection;
import com.cta4j.bus.api.core.external.CtaError;
import com.cta4j.bus.api.core.external.CtaResponse;
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
public final class DirectionsApiImpl implements DirectionsApi {
    private static final String DIRECTIONS_ENDPOINT = String.format("%s/getdirections", ApiUtils.API_PREFIX);

    private final BusApiContext context;

    public DirectionsApiImpl(BusApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<String> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(DIRECTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaDirection>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaDirection>> directionsResponse;

        try {
            directionsResponse = this.context.objectMapper()
                                             .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", DIRECTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaDirection>> bustimeResponse = directionsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaDirection> directions = bustimeResponse.data();

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(DIRECTIONS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((directions == null) || directions.isEmpty()) {
            return List.of();
        }

        return directions.stream()
                         .map(CtaDirection::id)
                         .toList();
    }
}
