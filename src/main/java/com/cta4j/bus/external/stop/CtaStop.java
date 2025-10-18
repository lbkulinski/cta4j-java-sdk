package com.cta4j.bus.external.stop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStop(
    String stpid,
    String stpnm,
    String lat,
    String lon
) {
}
