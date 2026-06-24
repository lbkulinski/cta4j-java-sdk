package com.cta4j.bus.common.internal.context;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.json.JsonMapper;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public record BusApiContext(
    String host,
    String apiKey,
    JsonMapper jsonMapper
) {
    public BusApiContext {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(jsonMapper);
    }
}
