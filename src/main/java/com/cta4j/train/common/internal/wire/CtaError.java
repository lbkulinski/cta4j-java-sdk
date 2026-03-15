package com.cta4j.train.common.internal.wire;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public record CtaError(int code, String message) {
    public CtaError {
        Objects.requireNonNull(message);

        if (code < 0) {
            throw new IllegalArgumentException("code must be non-negative");
        }

        if (message.isBlank()) {
            throw new IllegalArgumentException("message must not be null or blank");
        }
    }
}
