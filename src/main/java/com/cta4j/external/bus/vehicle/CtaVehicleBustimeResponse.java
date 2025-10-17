package com.cta4j.external.bus.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaVehicleBustimeResponse(
    List<CtaVehicle> vehicle
) {
}
