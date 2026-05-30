package com.cta4j.bus.detour.internal.wire;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
@NullMarked
public record CtaDetourError(
    String msg,
    @Nullable String rt,
    @Nullable String rtdir,
    @Nullable String rtpidatafeed
) implements CtaError {
    public CtaDetourError {
        Objects.requireNonNull(msg);
    }
}
