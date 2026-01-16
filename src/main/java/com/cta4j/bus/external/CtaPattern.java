package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
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
        if (rtdir == null) {
            throw new IllegalArgumentException("rtdir must not be null");
        }

        if (pt == null) {
            throw new IllegalArgumentException("pt must not be null");
        }

        for (CtaPoint point : pt) {
            if (point == null) {
                throw new IllegalArgumentException("pt must not contain null elements");
            }
        }
    }
}
