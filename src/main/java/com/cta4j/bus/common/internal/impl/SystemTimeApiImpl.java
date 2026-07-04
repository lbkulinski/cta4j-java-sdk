package com.cta4j.bus.common.internal.impl;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.mapper.Qualifiers;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.common.internal.wire.CtaTimeBustimeResponse;
import com.cta4j.bus.common.internal.wire.CtaTimeError;
import com.cta4j.common.internal.http.HttpClient;
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
            .setPath(BusApiConstants.SYSTEM_TIME_ENDPOINT)
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        String response = HttpClient.get(url);

        CtaResponse<CtaTimeBustimeResponse> timeResponse;

        try {
            timeResponse = JsonMapper.shared()
                                     .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jBusException("Failed to parse response", BusApiConstants.SYSTEM_TIME_ENDPOINT, e);
        }

        CtaTimeBustimeResponse bustimeResponse = timeResponse.bustimeResponse();

        String systemTime = bustimeResponse.tm();
        List<CtaTimeError> errors = bustimeResponse.error();

        if (systemTime != null) {
            Instant timestamp = Qualifiers.mapTimestamp(systemTime);

            return Objects.requireNonNull(timestamp);
        }

        if (errors == null || errors.isEmpty()) {
            throw new Cta4jBusException("No system time data returned", BusApiConstants.SYSTEM_TIME_ENDPOINT);
        }

        throw new Cta4jBusException(errors, BusApiConstants.SYSTEM_TIME_ENDPOINT);
    }
}
