package com.cta4j.mapper.bus;

import com.cta4j.external.bus.vehicle.CtaVehicle;
import com.cta4j.model.bus.Bus;

import java.math.BigDecimal;
import java.util.Objects;

public final class BusMapper {
    private BusMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Bus fromExternal(CtaVehicle vehicle) {
        Objects.requireNonNull(vehicle);

        return new Bus(
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
