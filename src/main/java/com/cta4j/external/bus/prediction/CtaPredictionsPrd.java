package com.cta4j.external.bus.prediction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPredictionsPrd(
    String tmstmp,
    String typ,
    String stpnm,
    String stpid,
    String vid,
    String dstp,
    String rt,
    String rtdd,
    String rtdir,
    String des,
    String prdtm,
    String dly,
    String dyn,
    String tablockid,
    String tatripid,
    String origtatripno,
    String prdctdn,
    String zone,
    String psgld,
    String stst,
    String stsd,
    String flagstop
) {
}
