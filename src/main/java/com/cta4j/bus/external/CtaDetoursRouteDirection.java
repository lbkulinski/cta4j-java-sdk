package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDetoursRouteDirection(
    String rt,

    String dir
) {
    public CtaDetoursRouteDirection {
        if (rt == null) {
            throw new IllegalArgumentException("rt must not be null");
        }

        if (dir == null) {
            throw new IllegalArgumentException("dir must not be null");
        }
    }
}
