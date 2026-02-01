package com.cta4j.bus.stop.internal.impl;

import com.cta4j.bus.internal.context.BusApiContext;
import com.cta4j.bus.internal.util.ApiUtils;
import com.cta4j.bus.stop.StopsApi;
import com.cta4j.bus.stop.internal.wire.CtaStop;
import com.cta4j.bus.stop.internal.mapper.StopMapper;
import com.cta4j.bus.stop.model.Stop;
import com.cta4j.bus.internal.wire.CtaBustimeResponse;
import com.cta4j.bus.internal.wire.CtaError;
import com.cta4j.bus.internal.wire.CtaResponse;
import com.cta4j.exception.Cta4jException;
import com.cta4j.internal.http.HttpClient;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class StopsApiImpl implements StopsApi {
    private static final String STOPS_ENDPOINT = String.format("%s/getstops", ApiUtils.API_PREFIX);
    private static final int MAX_STOP_IDS_PER_REQUEST = 10;

    private final BusApiContext context;

    public StopsApiImpl(BusApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<Stop> findByRouteIdAndDirection(String routeId, String direction) {
        Objects.requireNonNull(routeId);
        Objects.requireNonNull(direction);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(STOPS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("dir", direction)
            .addParameter("key", this.context.apiKey())
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

        stopIds.forEach(Objects::requireNonNull);

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
            .setHost(this.context.host())
            .setPath(STOPS_ENDPOINT)
            .addParameter("stpid", stopIdsString)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<Stop> makeRequest(String url) {
        String response = HttpClient.get(url);

        TypeReference<CtaResponse<List<CtaStop>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaStop>> stopsResponse;

        try {
            stopsResponse = this.context.objectMapper()
                                        .readValue(response, typeReference);
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
                    .map(StopMapper.INSTANCE::toDomain)
                    .toList();
    }
}
