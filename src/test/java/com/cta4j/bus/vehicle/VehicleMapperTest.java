package com.cta4j.bus.vehicle;

import com.cta4j.bus.vehicle.internal.mapper.VehicleMapper;
import com.cta4j.bus.vehicle.internal.wire.CtaVehicle;
import com.cta4j.bus.vehicle.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class VehicleMapperTest {
    @Test
    void toDomain_mapsRequiredFields() {
        CtaVehicle wire = new CtaVehicle(
            "509", null, "20200308 10:28:00",
            41.88, -87.63, 180, 3630,
            "8", "Waveland/Broadway", 1000,
            null, null, null, null, null,
            false, null, null, null,
            "block1", "trip1", "original1", "",
            1, "EMPTY", null, null
        );

        Vehicle vehicle = VehicleMapper.INSTANCE.toDomain(wire);

        assertThat(vehicle.id()).isEqualTo("509");
        assertThat(vehicle.routeId()).isEqualTo("8");
        assertThat(vehicle.destination()).isEqualTo("Waveland/Broadway");
        assertThat(vehicle.delayed()).isFalse();
        assertThat(vehicle.coordinates().latitude()).isEqualByComparingTo("41.88");
        assertThat(vehicle.coordinates().longitude()).isEqualByComparingTo("-87.63");
    }

    @Test
    void toDomain_mapsDelayedTrue() {
        CtaVehicle wire = new CtaVehicle(
            "509", null, "20200308 10:28:00",
            41.88, -87.63, 180, 3630,
            "8", "Waveland/Broadway", 1000,
            null, null, null, null, null,
            true, null, null, null,
            "block1", "trip1", "original1", "",
            1, "EMPTY", null, null
        );

        Vehicle vehicle = VehicleMapper.INSTANCE.toDomain(wire);

        assertThat(vehicle.delayed()).isTrue();
    }
}
