package com.cta4j.bus.pattern.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPattern(
    int pid,

    int ln,

    String rtdir,

    List<CtaPoint> pt,

    @Nullable
    String dtrid,

    @Nullable
    List<CtaPoint> dtrpt
) {
    public CtaPattern {
        Objects.requireNonNull(rtdir);
        Objects.requireNonNull(pt);

        pt = List.copyOf(pt);

        if (dtrpt != null) {
            dtrpt = List.copyOf(dtrpt);
        }
    }
}
