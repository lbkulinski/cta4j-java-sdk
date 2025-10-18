package com.cta4j.bus.external.route;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaRoute(
    String rt,
    String rtnm,
    String rtdd,
    String rtclr
) {
}
