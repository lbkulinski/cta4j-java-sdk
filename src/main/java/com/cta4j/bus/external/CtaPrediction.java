package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPrediction(
    String tmstmp,

    String typ,

    String stpid,

    String stpnm,

    int vid,

    int dstp,

    String rt,

    String rtdd,

    String rtdir,

    String des,

    String prdtm,

    @Nullable
    Boolean dly,

    int dyn,

    String tablockid,

    String tatripid,

    String origtatripno,

    String zone,

    String psgld,

    @Nullable
    Integer gtfsseq,

    @Nullable
    String nbus,

    @Nullable
    Integer stst,

    @Nullable
    String stsd,

    int flagstop
) {
    public CtaPrediction {
        if (tmstmp == null) {
            throw new IllegalArgumentException("tmstmp must not be null");
        }

        if (typ == null) {
            throw new IllegalArgumentException("typ must not be null");
        }

        if (stpid == null) {
            throw new IllegalArgumentException("stpid must not be null");
        }

        if (stpnm == null) {
            throw new IllegalArgumentException("stpnm must not be null");
        }

        if (rt == null) {
            throw new IllegalArgumentException("rt must not be null");
        }

        if (rtdd == null) {
            throw new IllegalArgumentException("rtdd must not be null");
        }

        if (rtdir == null) {
            throw new IllegalArgumentException("rtdir must not be null");
        }

        if (des == null) {
            throw new IllegalArgumentException("des must not be null");
        }

        if (prdtm == null) {
            throw new IllegalArgumentException("prdtm must not be null");
        }

        if (tablockid == null) {
            throw new IllegalArgumentException("tablockid must not be null");
        }

        if (tatripid == null) {
            throw new IllegalArgumentException("tatripid must not be null");
        }

        if (origtatripno == null) {
            throw new IllegalArgumentException("origtatripno must not be null");
        }

        if (zone == null) {
            throw new IllegalArgumentException("zone must not be null");
        }

        if (psgld == null) {
            throw new IllegalArgumentException("psgld must not be null");
        }
    }
}
