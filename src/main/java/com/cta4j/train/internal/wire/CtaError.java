package com.cta4j.train.internal.wire;

public record CtaError(int code, String message) {
    public CtaError {
        if (code < 0) {
            throw new IllegalArgumentException("code must be non-negative");
        }

        if ((message == null) || message.isBlank()) {
            throw new IllegalArgumentException("message must not be null or blank");
        }
    }
}
