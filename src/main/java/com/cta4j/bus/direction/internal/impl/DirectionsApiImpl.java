package com.cta4j.bus.direction.internal.impl;

import com.cta4j.bus.common.BusApiConstants;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.direction.DirectionsApi;
import com.cta4j.bus.direction.internal.wire.CtaDirection;
import com.cta4j.bus.direction.internal.wire.CtaDirectionBustimeResponse;
import com.cta4j.bus.direction.internal.wire.CtaDirectionError;
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
public final class DirectionsApiImpl implements DirectionsApi {
    private static final Logger log = LoggerFactory.getLogger(DirectionsApiImpl.class);

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

        String response = HttpClient.get(url);

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

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", BusApiConstants.DIRECTIONS_ENDPOINT);

            return List.of();
        }

        boolean notFound = errors.stream()
                                 .allMatch(error -> error.rt() != null);

        if (notFound) {
            return List.of();
        }

        throw new Cta4jBusException(errors, BusApiConstants.DIRECTIONS_ENDPOINT);
    }
}
