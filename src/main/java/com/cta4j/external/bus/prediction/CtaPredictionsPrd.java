package com.cta4j.external.bus.prediction;

public record CtaPredictionsPrd(
    String tmstmp,
    String typ,
    String stpnm,
    String stpid,
    String vid,
    int dstp,
    String rt,
    String rtdd,
    String rtdir,
    String des,
    String prdtm,
    boolean dly,
    int dyn,
    String tablockid,
    String tatripid,
    String origtatripno,
    String prdctdn,
    String zone,
    String psgld,
    int stst,
    String stsd,
    int flagstop
) {
}
