package com.cta4j.train.common.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaResponse<T>(T ctatt) {
    public CtaResponse {
        Objects.requireNonNull(ctatt);
    }
}
