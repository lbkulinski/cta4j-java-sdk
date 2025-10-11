package com.cta4j.model.bus;

import com.cta4j.external.bus.vehicle.CtaVehicle;

import java.math.BigDecimal;
import java.util.Objects;

public record Vehicle(
    String id,

    BigDecimal latitude,

    BigDecimal longitude,

    int heading,

    String route,

    String destination,

    boolean delayed
) {
    public static Vehicle fromExternal(CtaVehicle vehicle) {
        Objects.requireNonNull(vehicle);

        return new Vehicle(
            vehicle.vid(),
            new BigDecimal(vehicle.lat()),
            new BigDecimal(vehicle.lon()),
            Integer.parseInt(vehicle.hdg()),
            vehicle.rt(),
            vehicle.des(),
            vehicle.dly()
        );
    }
}
