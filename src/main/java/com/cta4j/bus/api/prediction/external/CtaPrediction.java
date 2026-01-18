package com.cta4j.bus.api.prediction.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
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
        Objects.requireNonNull(tmstmp);
        Objects.requireNonNull(typ);
        Objects.requireNonNull(stpid);
        Objects.requireNonNull(stpnm);
        Objects.requireNonNull(rt);
        Objects.requireNonNull(rtdd);
        Objects.requireNonNull(rtdir);
        Objects.requireNonNull(des);
        Objects.requireNonNull(prdtm);
        Objects.requireNonNull(tablockid);
        Objects.requireNonNull(tatripid);
        Objects.requireNonNull(origtatripno);
        Objects.requireNonNull(zone);
        Objects.requireNonNull(psgld);
    }
}
