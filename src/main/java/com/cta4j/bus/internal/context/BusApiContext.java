package com.cta4j.bus.internal.context;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public record BusApiContext(
    String host,

    String apiKey,

    ObjectMapper objectMapper
) {
    public BusApiContext {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(objectMapper);
    }
}
