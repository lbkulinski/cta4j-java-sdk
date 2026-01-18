package com.cta4j.bus.api.pattern.impl;

import com.cta4j.bus.api.ApiUtils;
import com.cta4j.bus.api.pattern.PatternsApi;
import com.cta4j.bus.api.pattern.external.CtaPattern;
import com.cta4j.bus.api.pattern.mapper.RoutePatternMapper;
import com.cta4j.bus.api.pattern.model.RoutePattern;
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
public final class PatternsApiImpl implements PatternsApi {
    private static final String PATTERNS_ENDPOINT = String.format("%s/getpatterns", ApiUtils.API_PREFIX);
    private static final int MAX_PATTERN_IDS_PER_REQUEST = 10;

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final RoutePatternMapper routePatternMapper;

    public PatternsApiImpl(
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
        this.routePatternMapper = Mappers.getMapper(RoutePatternMapper.class);
    }

    @Override
    public List<RoutePattern> findByIds(@Nullable Collection<@Nullable String> patternIds) {
        if (patternIds == null) {
            throw new IllegalArgumentException("patternIds must not be null");
        }

        for (String patternId : patternIds) {
            if (patternId == null) {
                throw new IllegalArgumentException("patternIds must not contain null elements");
            }
        }

        if (patternIds.size() > MAX_PATTERN_IDS_PER_REQUEST) {
            String message = String.format(
                "A maximum of %d pattern IDs can be requested at once, but %d were provided",
                MAX_PATTERN_IDS_PER_REQUEST,
                patternIds.size()
            );

            throw new IllegalArgumentException(message);
        }

        String patternIdsString = String.join(",", patternIds);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(PATTERNS_ENDPOINT)
            .addParameter("pid", patternIdsString)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<RoutePattern> findByRouteId(@Nullable String routeId) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(PATTERNS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<RoutePattern> makeRequest(String url) {
        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaPattern>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaPattern>> patternsResponse;

        try {
            patternsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", PATTERNS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaPattern>> bustimeResponse = patternsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaPattern> patterns = bustimeResponse.data();

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(PATTERNS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((patterns == null) || patterns.isEmpty()) {
            return List.of();
        }

        return patterns.stream()
                       .map(this.routePatternMapper::toDomain)
                       .toList();
    }
}
