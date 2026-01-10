package com.cta4j.bus.external.stop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStop(
    String stpid,
    String stpnm,
    double lat,
    double lon,
    List<Integer> dtradd,
    List<Integer> dtrrem,
    Integer gtfsseq,
    Boolean ada
) {
}
