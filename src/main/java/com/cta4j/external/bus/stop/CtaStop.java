package com.cta4j.external.bus.stop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStop(
    String stpid,
    String stpnm,
    String lat,
    String lon
) {
}
