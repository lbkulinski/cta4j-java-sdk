package com.cta4j.bus.route.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaRoute(
    String rt,
    String rtnm,
    String rtclr,
    String rtdd,
    @Nullable String rtpidatafeed
) {
    public CtaRoute {
        Objects.requireNonNull(rt);
        Objects.requireNonNull(rtnm);
        Objects.requireNonNull(rtclr);
        Objects.requireNonNull(rtdd);
    }
}
