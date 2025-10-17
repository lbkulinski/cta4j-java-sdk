package com.cta4j.external.bus.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
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
