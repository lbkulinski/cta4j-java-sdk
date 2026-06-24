package com.cta4j.bus.pattern.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaPoint(
    int seq,
    String typ,
    @Nullable String stpid,
    @Nullable String stpnm,
    @Nullable Float pdist,
    double lat,
    double lon
) {
    public CtaPoint {
        Objects.requireNonNull(typ);
    }
}
