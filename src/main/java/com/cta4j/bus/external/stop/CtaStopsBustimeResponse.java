package com.cta4j.bus.external.stop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStopsBustimeResponse(
    List<CtaStop> stops
) {
}
