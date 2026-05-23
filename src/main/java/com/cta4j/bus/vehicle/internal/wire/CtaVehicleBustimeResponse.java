package com.cta4j.bus.vehicle.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
@NullMarked
public record CtaVehicleBustimeResponse(
    @Nullable
    List<CtaVehicleError> error,

    @Nullable
    List<CtaVehicle> vehicle
) {
}
