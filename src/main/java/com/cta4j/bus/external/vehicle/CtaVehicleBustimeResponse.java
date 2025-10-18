package com.cta4j.bus.external.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaVehicleBustimeResponse(
    List<CtaVehicle> vehicle
) {
}
