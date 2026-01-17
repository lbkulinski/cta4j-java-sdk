package com.cta4j.bus.api.vehicle.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaVehicle(
    String vid,

    @Nullable
    String rtpidatafeed,

    @Nullable
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
    public CtaVehicle(
        @Nullable String vid,
        @Nullable String rtpidatafeed,
        @Nullable String tmpstmp,
        double lat,
        double lon,
        int hdg,
        int pid,
        @Nullable String rt,
        @Nullable String des,
        int pdist,
        @Nullable Integer stopstatus,
        @Nullable Integer timepointid,
        @Nullable String stopid,
        @Nullable Integer sequence,
        @Nullable Integer gtfsseq,
        boolean dly,
        @Nullable String srvtmstmp,
        @Nullable Integer spd,
        @Nullable Integer blk,
        @Nullable String tablockid,
        @Nullable String tatripid,
        @Nullable String origtatripno,
        @Nullable String zone,
        int mode,
        @Nullable String psgld,
        @Nullable Integer stst,
        @Nullable String stsd
    ) {
        if (vid == null) {
            throw new IllegalArgumentException("vid must not be null");
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

        this.vid = vid;
        this.rtpidatafeed = rtpidatafeed;
        this.tmpstmp = tmpstmp;
        this.lat = lat;
        this.lon = lon;
        this.hdg = hdg;
        this.pid = pid;
        this.rt = rt;
        this.des = des;
        this.pdist = pdist;
        this.stopstatus = stopstatus;
        this.timepointid = timepointid;
        this.stopid = stopid;
        this.sequence = sequence;
        this.gtfsseq = gtfsseq;
        this.dly = dly;
        this.srvtmstmp = srvtmstmp;
        this.spd = spd;
        this.blk = blk;
        this.tablockid = tablockid;
        this.tatripid = tatripid;
        this.origtatripno = origtatripno;
        this.zone = zone;
        this.mode = mode;
        this.psgld = psgld;
        this.stst = stst;
        this.stsd = stsd;
    }
}
