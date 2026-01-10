package com.cta4j.bus.external.prediction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPredictionsPrd(
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

    Boolean dly,

    int dyn,

    String tablockid,

    String tatripid,

    String origtatripno,

    String zone,

    String psgld,

    int gtfsseq,

    String nbus,

    Integer stst,

    String stsd,

    int flagstop
) {
}
