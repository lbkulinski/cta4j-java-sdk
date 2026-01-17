package com.cta4j.bus.api.route.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaRoute(
    String rt,

    String rtnm,

    String rtclr,

    String rtdd,

    @Nullable
    String rtpidatafeed
) {
    public CtaRoute(
        @Nullable String rt,
        @Nullable String rtnm,
        @Nullable String rtclr,
        @Nullable String rtdd,
        @Nullable String rtpidatafeed
    ) {
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

        this.rt = rt;
        this.rtnm = rtnm;
        this.rtclr = rtclr;
        this.rtdd = rtdd;
        this.rtpidatafeed = rtpidatafeed;
    }
}
