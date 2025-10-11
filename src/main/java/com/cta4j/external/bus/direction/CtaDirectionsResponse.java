package com.cta4j.external.bus.direction;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CtaDirectionsResponse(
    @JsonAlias("bustime-response")
    CtaDirectionsBustimeResponse bustimeResponse
) {
}
