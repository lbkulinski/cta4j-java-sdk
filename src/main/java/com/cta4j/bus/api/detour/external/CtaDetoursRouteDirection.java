package com.cta4j.bus.api.detour.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDetoursRouteDirection(
    String rt,

    String dir
) {
    public CtaDetoursRouteDirection {
        Objects.requireNonNull(rt);
        Objects.requireNonNull(dir);
    }
}
