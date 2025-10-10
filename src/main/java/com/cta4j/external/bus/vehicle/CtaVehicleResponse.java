package com.cta4j.external.bus.vehicle;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CtaVehicleResponse(
    @JsonAlias("bustime-response")
    CtaVehicleBustimeResponse bustimeResponse
) {
}
