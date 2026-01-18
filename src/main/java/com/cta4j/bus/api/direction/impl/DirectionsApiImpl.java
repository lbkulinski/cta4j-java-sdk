package com.cta4j.bus.api.direction.impl;

import com.cta4j.bus.api.ApiUtils;
import com.cta4j.bus.api.direction.DirectionsApi;
import com.cta4j.bus.external.CtaBustimeResponse;
import com.cta4j.bus.api.direction.external.CtaDirection;
import com.cta4j.bus.external.CtaError;
import com.cta4j.bus.external.CtaResponse;
import com.cta4j.exception.Cta4jException;
import com.cta4j.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@NullMarked
public final class DirectionsApiImpl implements DirectionsApi {
    private static final String DIRECTIONS_ENDPOINT = String.format("%s/getdirections", ApiUtils.API_PREFIX);

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public DirectionsApiImpl(
        String host,
        String apiKey,
        ObjectMapper objectMapper
    ) {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(objectMapper);

        this.host = host;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<String> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(DIRECTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaDirection>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaDirection>> directionsResponse;

        try {
            directionsResponse = this.objectMapper.readValue(response, typeReference);
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
