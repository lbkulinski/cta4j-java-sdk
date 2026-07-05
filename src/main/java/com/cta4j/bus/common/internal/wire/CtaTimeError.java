package com.cta4j.bus.common.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaTimeError(String msg) implements CtaError {
    public CtaTimeError {
        Objects.requireNonNull(msg);
    }
}
