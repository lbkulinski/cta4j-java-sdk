package com.cta4j.alert.common.internal.config;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public record AlertApiConfig(
    String scheme,
    String host,
    int port
) {
    public AlertApiConfig {
        Objects.requireNonNull(scheme);
        Objects.requireNonNull(host);
    }

    public AlertApiConfig(String scheme, String host) {
        this(scheme, host, -1);
    }
}
