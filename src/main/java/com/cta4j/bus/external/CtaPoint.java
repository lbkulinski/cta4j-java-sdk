package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPoint(
    int seq,

    String typ,

    @Nullable
    String stpid,

    @Nullable
    String stpnm,

    @Nullable
    Float pdist,

    double lat,

    double lon
) {
    public CtaPoint {
        if (typ == null) {
            throw new IllegalArgumentException("typ must not be null");
        }
    }
}
