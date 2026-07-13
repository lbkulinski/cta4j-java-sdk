package com.cta4j.alert.common.internal.config;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public record AlertApiConfig(
    String scheme,
    String host,
    int port,
    String apiKey
) {
    public AlertApiConfig {
        Objects.requireNonNull(scheme);
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
    }

    public AlertApiConfig(String scheme, String host, String apiKey) {
        this(scheme, host, -1, apiKey);
    }
}
