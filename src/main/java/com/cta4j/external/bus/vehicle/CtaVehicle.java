package com.cta4j.external.bus.vehicle;

public record CtaVehicle(
    String vid,
    String tmstmp,
    String lat,
    String lon,
    String hdg,
    String pid,
    String rt,
    String des,
    String pdist,
    Boolean dly,
    String tatripid,
    String origtatripno,
    String tablockid,
    String zone,
    String mode,
    String psgld,
    String stst,
    String stsd
) {
}
