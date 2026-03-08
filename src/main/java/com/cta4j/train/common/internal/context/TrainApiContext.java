package com.cta4j.train.common.internal.context;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public record TrainApiContext(
    String host,

    String stationsUrl,

    String apiKey,

    ObjectMapper objectMapper
) {
    public TrainApiContext {
        Objects.requireNonNull(host);
        Objects.requireNonNull(stationsUrl);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(objectMapper);
    }
}
