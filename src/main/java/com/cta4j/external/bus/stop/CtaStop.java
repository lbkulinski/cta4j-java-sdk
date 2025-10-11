package com.cta4j.external.bus.stop;

public record CtaStop(
    String stpid,
    String stpnm,
    String lat,
    String lon
) {
}
