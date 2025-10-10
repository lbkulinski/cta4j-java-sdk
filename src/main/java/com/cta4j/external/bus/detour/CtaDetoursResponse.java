package com.cta4j.external.bus.detour;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CtaDetoursResponse(
    @JsonAlias("bustime-response")
    CtaDetoursBustimeResponse bustimeResponse
) {
}
