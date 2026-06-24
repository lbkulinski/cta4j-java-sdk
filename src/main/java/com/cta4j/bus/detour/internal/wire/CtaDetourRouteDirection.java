package com.cta4j.bus.detour.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaDetourRouteDirection(
    String rt,
    String dir
) {
    public CtaDetourRouteDirection {
        Objects.requireNonNull(rt);
        Objects.requireNonNull(dir);
    }
}
