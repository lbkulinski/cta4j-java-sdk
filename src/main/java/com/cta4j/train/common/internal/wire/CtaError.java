package com.cta4j.train.common.internal.wire;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
public record CtaError(int code, String message) {
    public CtaError(int code, @Nullable String message) {
        if (code < 0) {
            throw new IllegalArgumentException("code must be non-negative");
        }

        if ((message == null) || message.isBlank()) {
            message = "An unknown error occurred.";
        }

        this.code = code;
        this.message = message;
    }
}
