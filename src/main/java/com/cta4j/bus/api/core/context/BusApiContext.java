package com.cta4j.bus.api.core.context;

import com.cta4j.bus.api.core.request.RequestOptions;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@NullMarked
public record BusApiContext(
    String host,

    String apiKey,

    RequestOptions defaultRequestOptions,

    ObjectMapper objectMapper
) {
    public BusApiContext {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(defaultRequestOptions);
        Objects.requireNonNull(objectMapper);
    }
}
