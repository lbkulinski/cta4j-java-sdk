package com.cta4j.bus.external.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaVehicle(
    String vid,

    String rtpidatafeed,

    String tmpstmp,

    Double lat,

    Double lon,

    Integer hdg,

    Integer pid,

    String rt,

    String des,

    Integer pdist,

    Byte stopstatus,

    Integer timepointid,

    String stopid,

    Integer sequence,

    Integer gtfsseq,

    Boolean dly,

    String srvtmstmp,

    Integer spd,

    Integer blk,

    String tablockid,

    String tatripid,

    String origtatripno,

    String zone,

    Integer mode,

    String psgld,

    Integer stst,

    String stsd
) {
}
