package com.cta4j.external.bus.direction;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDirectionsResponse(
    @JsonAlias("bustime-response")
    CtaDirectionsBustimeResponse bustimeResponse
) {
}
