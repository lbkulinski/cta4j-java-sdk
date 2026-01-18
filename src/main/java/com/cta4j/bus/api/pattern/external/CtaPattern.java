package com.cta4j.bus.api.pattern.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

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
    public CtaPattern(
        int pid,
        int ln,
        @Nullable String rtdir,
        @Nullable List<@Nullable CtaPoint> pt,
        @Nullable String dtrid,
        @Nullable List<@Nullable CtaPoint> dtrpt
    ) {
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

        List<CtaPoint> dtrptCopy = null;

        if (dtrpt != null) {
            for (CtaPoint point : dtrpt) {
                if (point == null) {
                    throw new IllegalArgumentException("dtrpt must not contain null elements");
                }
            }

            dtrptCopy = List.copyOf(dtrpt);
        }

        this.pid = pid;
        this.ln = ln;
        this.rtdir = rtdir;
        this.pt = List.copyOf(pt);
        this.dtrid = dtrid;
        this.dtrpt = dtrptCopy;
    }
}
