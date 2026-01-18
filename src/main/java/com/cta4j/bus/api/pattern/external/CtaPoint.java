package com.cta4j.bus.api.pattern.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
    public CtaPoint(
        int seq,
        @Nullable String typ,
        @Nullable String stpid,
        @Nullable String stpnm,
        @Nullable Float pdist,
        double lat,
        double lon
    ) {
        if (typ == null) {
            throw new IllegalArgumentException("typ must not be null");
        }

        this.seq = seq;
        this.typ = typ;
        this.stpid = stpid;
        this.stpnm = stpnm;
        this.pdist = pdist;
        this.lat = lat;
        this.lon = lon;
    }
}
