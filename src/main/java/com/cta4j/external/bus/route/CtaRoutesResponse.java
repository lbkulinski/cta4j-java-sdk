package com.cta4j.external.bus.route;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CtaRoutesResponse(
    @JsonAlias("bustime-response")
    CtaRoutesBustimeResponse bustimeResponse
) {
}
