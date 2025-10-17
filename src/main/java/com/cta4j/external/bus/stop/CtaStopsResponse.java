package com.cta4j.external.bus.stop;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStopsResponse(
    @JsonAlias("bustime-response")
    CtaStopsBustimeResponse bustimeResponse
) {
}
