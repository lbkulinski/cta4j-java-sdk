package com.cta4j.external.bus.stop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStopsBustimeResponse(
    List<CtaStop> stops
) {
}
