package com.cta4j.bus.pattern.internal.impl;

import com.cta4j.bus.common.BusApiConstants;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.pattern.PatternsApi;
import com.cta4j.bus.pattern.internal.wire.CtaPattern;
import com.cta4j.bus.pattern.internal.mapper.RoutePatternMapper;
import com.cta4j.bus.pattern.internal.wire.CtaPatternBustimeResponse;
import com.cta4j.bus.pattern.internal.wire.CtaPatternError;
import com.cta4j.bus.pattern.model.RoutePattern;
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
public final class PatternsApiImpl implements PatternsApi {
    private static final Logger log = LoggerFactory.getLogger(PatternsApiImpl.class);

    private static final TypeReference<CtaResponse<CtaPatternBustimeResponse>> TYPE_REFERENCE =
        new TypeReference<>() {};

    private final BusApiConfig config;

    public PatternsApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<RoutePattern> findByIds(Collection<String> patternIds) {
        Objects.requireNonNull(patternIds);

        if (patternIds.isEmpty()) {
            return List.of();
        }

        ApiUtils.requireMaxIds(patternIds, "pattern");

        patternIds = List.copyOf(patternIds);

        String patternIdsString = String.join(",", patternIds);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.PATTERNS_ENDPOINT)
            .addParameter("pid", patternIdsString)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<RoutePattern> findByRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.PATTERNS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<RoutePattern> makeRequest(String url) {
        String response = HttpClient.get(url);

        CtaResponse<CtaPatternBustimeResponse> patternsResponse;

        try {
            patternsResponse = JsonMapper.shared()
                                         .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jBusException("Failed to parse response", BusApiConstants.PATTERNS_ENDPOINT, e);
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
            log.warn("Received empty response from {}", BusApiConstants.PATTERNS_ENDPOINT);

            return List.of();
        }

        boolean notFound = errors.stream()
                                 .allMatch(error -> error.pid() != null || error.rt() != null);

        if (notFound) {
            return List.of();
        }

        throw new Cta4jBusException(errors, BusApiConstants.PATTERNS_ENDPOINT);
    }
}
