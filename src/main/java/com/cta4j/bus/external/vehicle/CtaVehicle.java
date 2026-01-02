package com.cta4j.bus.external.vehicle;

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

    String tmpstmp,

    Double lat,

    Double lon,

    Integer hdg,

    Integer pid,

    String rt,
    String des,

    Integer pdist,

    @Nullable
    Byte stopstatus,

    @Nullable
    Integer timepointid,

    @Nullable
    String stopid,

    @Nullable
    Integer sequence,

    @Nullable
    Integer gtfsseq,

    Boolean dly,

    @Nullable
    String srvtmstmp,

    Integer spd,

    @Nullable
    Integer blk,

    String tablockid,

    String tatripid,

    String origtatripno,

    String zone,

    Byte mode,

    String psgld,

    @Nullable
    Integer stst,

    @Nullable
    String stsd
) {
}
