package com.cta4j.bus.stop.internal.impl;

import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.stop.StopsApi;
import com.cta4j.bus.stop.internal.wire.CtaStop;
import com.cta4j.bus.stop.internal.mapper.StopMapper;
import com.cta4j.bus.stop.internal.wire.CtaStopBustimeResponse;
import com.cta4j.bus.stop.internal.wire.CtaStopError;
import com.cta4j.bus.stop.model.Stop;
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

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class StopsApiImpl implements StopsApi {
    private static final Logger log = LoggerFactory.getLogger(StopsApiImpl.class);

    private static final String STOPS_ENDPOINT = "%s/getstops".formatted(ApiUtils.API_PREFIX);
    private static final int MAX_STOP_IDS_PER_REQUEST = 10;
    private static final TypeReference<CtaResponse<CtaStopBustimeResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    private final BusApiConfig config;

    public StopsApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Stop> findByRouteIdAndDirection(String routeId, String direction) {
        Objects.requireNonNull(routeId);
        Objects.requireNonNull(direction);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(STOPS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("dir", direction)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Stop> findByIds(Collection<String> stopIds) {
        Objects.requireNonNull(stopIds);

        if (stopIds.isEmpty()) {
            return List.of();
        }

        if (stopIds.size() > MAX_STOP_IDS_PER_REQUEST) {
            String message = "A maximum of %d stop IDs can be requested at once, but %d were provided".formatted(
                MAX_STOP_IDS_PER_REQUEST,
                stopIds.size()
            );

            throw new IllegalArgumentException(message);
        }

        stopIds = List.copyOf(stopIds);

        String stopIdsString = String.join(",", stopIds);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(STOPS_ENDPOINT)
            .addParameter("stpid", stopIdsString)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<Stop> makeRequest(String url) {
        String response = HttpClient.get(url);

        CtaResponse<CtaStopBustimeResponse> stopsResponse;

        try {
            stopsResponse = JsonMapper.shared()
                                      .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(STOPS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaStopBustimeResponse bustimeResponse = stopsResponse.bustimeResponse();

        List<CtaStop> stops = bustimeResponse.stops();
        List<CtaStopError> errors = bustimeResponse.error();

        if (stops != null && !stops.isEmpty()) {
            return stops.stream()
                        .map(StopMapper.INSTANCE::toDomain)
                        .toList();
        }

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", STOPS_ENDPOINT);

            return List.of();
        }

        boolean notFound = errors.stream()
                                 .allMatch(error ->
                                     (error.rt() != null && error.dir() != null) || error.stpid() != null);

        if (notFound) {
            return List.of();
        }

        String message = ApiUtils.buildErrorMessage(STOPS_ENDPOINT, errors);

        throw new Cta4jException(message);
    }
}
