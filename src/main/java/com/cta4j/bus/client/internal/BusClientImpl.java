package com.cta4j.bus.client.internal;

import com.cta4j.bus.client.BusClient;
import com.cta4j.bus.external.CtaBustimeResponse;
import com.cta4j.bus.external.CtaError;
import com.cta4j.bus.external.CtaResponse;
import com.cta4j.bus.api.prediction.mapper.PredictionMapper;
import com.cta4j.bus.mapper.DetourMapper;
import com.cta4j.bus.model.Detour;
import com.cta4j.exception.Cta4jException;
import com.cta4j.bus.external.CtaDetour;
import com.cta4j.util.HttpUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
public final class BusClientImpl implements BusClient {
    private static final Logger log = LoggerFactory.getLogger(BusClientImpl.class);

    private static final String SCHEME = "https";
    private static final String DEFAULT_HOST = "ctabustracker.com";
    private static final String API_PREFIX = "/bustime/api/v3";
    private static final String DETOURS_ENDPOINT = String.format("%s/getdetours", API_PREFIX);

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final DetourMapper detourMapper;

    private BusClientImpl(String host, String apiKey) {
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }

        if (apiKey == null) {
            throw new IllegalArgumentException("apiKey must not be null");
        }

        this.host = host;
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
        this.detourMapper = Mappers.getMapper(DetourMapper.class);
    }

    @Override
    public List<Detour> findDetoursByRouteIdAndDirection(String routeId, String direction) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(DETOURS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("dir", direction)
            .addParameter("tmres", "s") //TODO: Is tmres an option for detours?
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaDetour>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaDetour>> detoursResponse;

        try {
            detoursResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", DETOURS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaDetour>> bustimeResponse = detoursResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaDetour> detours = bustimeResponse.data();

        if ((errors == null) && (detours == null)) {
            log.debug("Detours bustime response missing both error and data from {}", DETOURS_ENDPOINT);

            return List.of();
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(DETOURS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((detours == null) || detours.isEmpty()) {
            return List.of();
        }

        return detours.stream()
                   .map(this.detourMapper::toDomain)
                   .toList();
    }

    private String buildErrorMessage(String endpoint, List<CtaError> errors) {
        String message = errors.stream()
                               .map(CtaError::msg)
                               .reduce("%s; %s"::formatted)
                               .orElse("Unknown error");

        return String.format("Error response from %s: %s", endpoint, message);
    }

    public static final class BuilderImpl implements BusClient.Builder {
        @Nullable
        private String host;

        @Nullable
        private String apiKey;

        public BuilderImpl() {
            this.host = null;
            this.apiKey = null;
        }

        @Override
        public Builder host(String host) {
            if (host == null) {
                throw new IllegalArgumentException("host must not be null");
            }

            this.host = host;

            return this;
        }

        @Override
        public Builder apiKey(String apiKey) {
            if (apiKey == null) {
                throw new IllegalArgumentException("apiKey must not be null");
            }

            this.apiKey = apiKey;

            return this;
        }

        @Override
        public BusClient build() {
            String finalHost = (this.host == null) ? DEFAULT_HOST : this.host;

            if (this.apiKey == null) {
                throw new IllegalStateException("API key must not be null");
            }

            return new BusClientImpl(finalHost, this.apiKey);
        }
    }
}
