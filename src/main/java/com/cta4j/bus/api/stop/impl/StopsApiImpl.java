package com.cta4j.bus.api.stop.impl;

import com.cta4j.bus.api.ApiUtils;
import com.cta4j.bus.api.stop.StopsApi;
import com.cta4j.bus.api.stop.external.CtaStop;
import com.cta4j.bus.api.stop.mapper.StopMapper;
import com.cta4j.bus.api.stop.model.Stop;
import com.cta4j.bus.external.CtaBustimeResponse;
import com.cta4j.bus.external.CtaError;
import com.cta4j.bus.external.CtaResponse;
import com.cta4j.exception.Cta4jException;
import com.cta4j.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.mapstruct.factory.Mappers;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.List;

@NullMarked
@ApiStatus.Internal
public final class StopsApiImpl implements StopsApi {
    private static final String STOPS_ENDPOINT = String.format("%s/getstops", ApiUtils.API_PREFIX);
    private static final int MAX_STOP_IDS_PER_REQUEST = 10;

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final StopMapper stopMapper;

    public StopsApiImpl(
        @Nullable String host,
        @Nullable String apiKey,
        @Nullable ObjectMapper objectMapper
    ) {
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }

        if (apiKey == null) {
            throw new IllegalArgumentException("apiKey must not be null");
        }

        if (objectMapper == null) {
            throw new IllegalArgumentException("objectMapper must not be null");
        }

        this.host = host;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
        this.stopMapper = Mappers.getMapper(StopMapper.class);
    }

    @Override
    public List<Stop> findByRouteIdAndDirection(@Nullable String routeId, @Nullable String direction) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(STOPS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("dir", direction)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Stop> findByIds(@Nullable Collection<@Nullable String> stopIds) {
        if (stopIds == null) {
            throw new IllegalArgumentException("stopIds must not be null");
        }

        for (String stopId : stopIds) {
            if (stopId == null) {
                throw new IllegalArgumentException("stopIds must not contain null elements");
            }
        }

        if (stopIds.size() > MAX_STOP_IDS_PER_REQUEST) {
            String message = String.format(
                "A maximum of %d stop IDs can be requested at once, but %d were provided",
                MAX_STOP_IDS_PER_REQUEST,
                stopIds.size()
            );

            throw new IllegalArgumentException(message);
        }

        String stopIdsString = String.join(",", stopIds);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(STOPS_ENDPOINT)
            .addParameter("stpid", stopIdsString)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<Stop> makeRequest(String url) {
        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaStop>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaStop>> stopsResponse;

        try {
            stopsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", STOPS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaStop>> bustimeResponse = stopsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaStop> stops = bustimeResponse.data();

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(STOPS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((stops == null) || stops.isEmpty()) {
            return List.of();
        }

        return stops.stream()
                    .map(this.stopMapper::toDomain)
                    .toList();
    }
}
