package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaVehicle(
    String vid,

    @Nullable
    String rtpidatafeed,

    String tmpstmp,

    double lat,

    double lon,

    int hdg,

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

    int spd,

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
        if (vid == null) {
            throw new IllegalArgumentException("vid must not be null");
        }

        if (tmpstmp == null) {
            throw new IllegalArgumentException("tmpstmp must not be null");
        }

        if (rt == null) {
            throw new IllegalArgumentException("rt must not be null");
        }

        if (des == null) {
            throw new IllegalArgumentException("des must not be null");
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
