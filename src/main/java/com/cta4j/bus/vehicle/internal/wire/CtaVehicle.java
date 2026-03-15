package com.cta4j.bus.vehicle.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaVehicle(
    String vid,

    @Nullable
    String rtpidatafeed,

    @Nullable
    String tmpstmp,

    @Nullable
    Double lat,

    @Nullable
    Double lon,

    @Nullable
    Integer hdg,

    int pid,

    String rt,

    String des,

    int pdist,

    @Nullable
    Integer stopstatus,

    @Nullable
    Integer timepointid,

    @Nullable
    String stopid,

    @Nullable
    Integer sequence,

    @Nullable
    Integer gtfsseq,

    boolean dly,

    @Nullable
    String srvtmstmp,

    @Nullable
    Integer spd,

    @Nullable
    Integer blk,

    String tablockid,

    String tatripid,

    String origtatripno,

    String zone,

    int mode,

    String psgld,

    @Nullable
    Integer stst,

    @Nullable
    String stsd
) {
    public CtaVehicle {
        Objects.requireNonNull(vid);
        Objects.requireNonNull(lat);
        Objects.requireNonNull(lon);
        Objects.requireNonNull(hdg);
        Objects.requireNonNull(rt);
        Objects.requireNonNull(des);
        Objects.requireNonNull(tablockid);
        Objects.requireNonNull(tatripid);
        Objects.requireNonNull(origtatripno);
        Objects.requireNonNull(zone);
        Objects.requireNonNull(psgld);
    }
}
