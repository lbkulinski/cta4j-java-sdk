package com.cta4j.bus.common.internal.config;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public record BusApiConfig(
    String host,
    String apiKey
) {
    public BusApiConfig {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
    }
}
