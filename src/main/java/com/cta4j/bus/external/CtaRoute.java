package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaRoute(
    String rt,

    String rtnm,

    String rtclr,

    String rtdd,

    @Nullable
    String rtpidatafeed
) {
    public CtaRoute {
        if (rt == null) {
            throw new IllegalArgumentException("rt must not be null");
        }

        if (rtnm == null) {
            throw new IllegalArgumentException("rtnm must not be null");
        }

        if (rtclr == null) {
            throw new IllegalArgumentException("rtclr must not be null");
        }

        if (rtdd == null) {
            throw new IllegalArgumentException("rtdd must not be null");
        }
    }
}
