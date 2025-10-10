package com.cta4j.external.bus.vehicle;

import java.util.List;

public record CtaVehicleBustimeResponse(
    List<CtaVehicle> vehicle
) {
}
