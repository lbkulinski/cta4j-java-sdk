package com.cta4j.external.bus.vehicle;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaVehicleResponse(
    @JsonAlias("bustime-response")
    CtaVehicleBustimeResponse bustimeResponse
) {
}
