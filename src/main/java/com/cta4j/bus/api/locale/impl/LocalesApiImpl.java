package com.cta4j.bus.api.locale.impl;

import com.cta4j.bus.api.locale.LocalesApi;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class LocalesApiImpl implements LocalesApi {
    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public LocalesApiImpl(String host, String apiKey, ObjectMapper objectMapper) {
        this.host = Objects.requireNonNull(host);
        this.apiKey = Objects.requireNonNull(apiKey);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }
}
