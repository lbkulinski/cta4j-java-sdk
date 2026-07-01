package com.cta4j.bus.common.internal.impl;

import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.mapper.Qualifiers;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.common.internal.wire.CtaTimeBustimeResponse;
import com.cta4j.bus.common.internal.wire.CtaTimeError;
import com.cta4j.common.internal.http.HttpClient;
import com.cta4j.exception.Cta4jException;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
final class SystemTimeApiImpl {
    private static final String SYSTEM_TIME_ENDPOINT = "%s/gettime".formatted(ApiUtils.API_PREFIX);
    private static final TypeReference<CtaResponse<CtaTimeBustimeResponse>> TYPE_REFERENCE =
        new TypeReference<>() {};

    private final BusApiConfig config;

    public SystemTimeApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    public Instant systemTime() {
        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(SYSTEM_TIME_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        String response = HttpClient.get(url);

        CtaResponse<CtaTimeBustimeResponse> timeResponse;

        try {
            timeResponse = JsonMapper.shared()
                                     .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(SYSTEM_TIME_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaTimeBustimeResponse bustimeResponse = timeResponse.bustimeResponse();

        String systemTime = bustimeResponse.tm();
        List<CtaTimeError> errors = bustimeResponse.error();

        if (systemTime != null) {
            Instant timestamp = Qualifiers.mapTimestamp(systemTime);

            return Objects.requireNonNull(timestamp);
        }

        if (errors == null || errors.isEmpty()) {
            String message = "No system time data returned from %s".formatted(SYSTEM_TIME_ENDPOINT);

            throw new Cta4jException(message);
        }

        String message = ApiUtils.buildErrorMessage(SYSTEM_TIME_ENDPOINT, errors);

        throw new Cta4jException(message);
    }
}
