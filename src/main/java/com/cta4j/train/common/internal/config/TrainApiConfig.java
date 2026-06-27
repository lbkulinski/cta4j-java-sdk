package com.cta4j.train.common.internal.config;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public record TrainApiConfig(
    String host,
    String stationsUrl,
    String apiKey
) {
    public TrainApiConfig {
        Objects.requireNonNull(host);
        Objects.requireNonNull(stationsUrl);
        Objects.requireNonNull(apiKey);
    }
}
