package com.cta4j.bus.direction.internal.impl;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.direction.DirectionsApi;
import com.cta4j.bus.direction.internal.wire.CtaDirection;
import com.cta4j.bus.direction.internal.wire.CtaDirectionBustimeResponse;
import com.cta4j.bus.direction.internal.wire.CtaDirectionError;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class DirectionsApiImpl implements DirectionsApi {
    private static final TypeReference<CtaResponse<CtaDirectionBustimeResponse>> TYPE_REFERENCE =
        new TypeReference<>() {};

    private final BusApiConfig config;

    public DirectionsApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<String> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.DIRECTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = Objects.requireNonNullElse(e.getMessage(), "Request failed");

            throw new Cta4jBusException(message, BusApiConstants.DIRECTIONS_ENDPOINT, e);
        }

        CtaResponse<CtaDirectionBustimeResponse> directionsResponse;

        try {
            directionsResponse = JsonMapper.shared()
                                           .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jBusException("Failed to parse response", BusApiConstants.DIRECTIONS_ENDPOINT, e);
        }

        CtaDirectionBustimeResponse bustimeResponse = directionsResponse.bustimeResponse();

        List<CtaDirection> directions = bustimeResponse.directions();
        List<CtaDirectionError> errors = bustimeResponse.error();

        if (directions != null && !directions.isEmpty()) {
            return directions.stream()
                             .map(CtaDirection::id)
                             .toList();
        }

        ApiUtils.checkErrors(errors, BusApiConstants.DIRECTIONS_ENDPOINT);

        return List.of();
    }
}
