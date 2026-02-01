package com.cta4j.train.internal.context;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public record TrainApiContext(
    String host,

    String apiKey,

    ObjectMapper objectMapper
) {
    public TrainApiContext {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(objectMapper);
    }
}
