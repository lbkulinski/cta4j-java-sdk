package com.cta4j.bus.pattern.internal.impl;

import com.cta4j.bus.common.internal.context.BusApiContext;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.pattern.PatternsApi;
import com.cta4j.bus.pattern.internal.wire.CtaPattern;
import com.cta4j.bus.pattern.internal.mapper.RoutePatternMapper;
import com.cta4j.bus.pattern.internal.wire.CtaPatternBustimeResponse;
import com.cta4j.bus.pattern.internal.wire.CtaPatternError;
import com.cta4j.bus.pattern.model.RoutePattern;
import com.cta4j.exception.Cta4jException;
import com.cta4j.common.internal.http.HttpClient;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class PatternsApiImpl implements PatternsApi {
    private static final Logger log = LoggerFactory.getLogger(PatternsApiImpl.class);

    private static final String PATTERNS_ENDPOINT = "%s/getpatterns".formatted(ApiUtils.API_PREFIX);
    private static final int MAX_PATTERN_IDS_PER_REQUEST = 10;

    private final BusApiContext context;

    public PatternsApiImpl(BusApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<RoutePattern> findByIds(Collection<String> patternIds) {
        Objects.requireNonNull(patternIds);

        if (patternIds.isEmpty()) {
            return List.of();
        }

        patternIds.forEach(Objects::requireNonNull);

        if (patternIds.size() > MAX_PATTERN_IDS_PER_REQUEST) {
            String message = "A maximum of %d pattern IDs can be requested at once, but %d were provided".formatted(
                MAX_PATTERN_IDS_PER_REQUEST,
                patternIds.size()
            );

            throw new IllegalArgumentException(message);
        }

        String patternIdsString = String.join(",", patternIds);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(PATTERNS_ENDPOINT)
            .addParameter("pid", patternIdsString)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<RoutePattern> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(PATTERNS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<RoutePattern> makeRequest(String url) {
        String response = HttpClient.get(url);

        TypeReference<CtaResponse<CtaPatternBustimeResponse>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaPatternBustimeResponse> patternsResponse;

        try {
            patternsResponse = this.context.jsonMapper()
                                           .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(PATTERNS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaPatternBustimeResponse bustimeResponse = patternsResponse.bustimeResponse();

        List<CtaPattern> patterns = bustimeResponse.ptr();
        List<CtaPatternError> errors = bustimeResponse.error();

        if (patterns != null && !patterns.isEmpty()) {
            return patterns.stream()
                           .map(RoutePatternMapper.INSTANCE::toDomain)
                           .toList();
        }

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", PATTERNS_ENDPOINT);

            return List.of();
        }

        boolean notFound = errors.stream()
                                 .allMatch(error -> error.pid() != null || error.rt() != null);

        if (notFound) {
            return List.of();
        }

        String message = ApiUtils.buildErrorMessage(PATTERNS_ENDPOINT, errors);

        throw new Cta4jException(message);
    }
}
