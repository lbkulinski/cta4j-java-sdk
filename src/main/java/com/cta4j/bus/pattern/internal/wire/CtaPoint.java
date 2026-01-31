package com.cta4j.bus.pattern.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
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
        Objects.requireNonNull(typ);
    }
}
