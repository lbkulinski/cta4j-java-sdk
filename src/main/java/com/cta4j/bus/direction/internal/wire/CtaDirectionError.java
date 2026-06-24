package com.cta4j.bus.direction.internal.wire;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaDirectionError(
    String msg,
    @Nullable String rt,
    @Nullable String rtpidatafeed
) implements CtaError {
    public CtaDirectionError {
        Objects.requireNonNull(msg);
    }
}
