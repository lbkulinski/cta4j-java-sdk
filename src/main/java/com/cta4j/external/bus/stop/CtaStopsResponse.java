package com.cta4j.external.bus.stop;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CtaStopsResponse(
    @JsonAlias("bustime-response")
    CtaStopsBustimeResponse bustimeResponse
) {
}
