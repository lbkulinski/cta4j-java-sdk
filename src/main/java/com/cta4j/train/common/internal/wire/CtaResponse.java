package com.cta4j.train.common.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaResponse<T>(T ctatt) {
    public CtaResponse {
        Objects.requireNonNull(ctatt);
    }
}
