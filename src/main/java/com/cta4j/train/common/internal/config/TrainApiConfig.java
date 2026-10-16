package com.cta4j.train.common.internal.config;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public record TrainApiConfig(
    String scheme,
    String host,
    int port,
    String apiKey
) {
    public TrainApiConfig {
        Objects.requireNonNull(scheme);
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
    }

    public TrainApiConfig(String scheme, String host, String apiKey) {
        this(scheme, host, -1, apiKey);
    }
}
