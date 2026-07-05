package com.cta4j.bus.detour;

import com.cta4j.bus.detour.internal.mapper.DetourMapper;
import com.cta4j.bus.detour.internal.wire.CtaDetour;
import com.cta4j.bus.detour.internal.wire.CtaDetourRouteDirection;
import com.cta4j.bus.detour.model.Detour;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DetourMapperTest {
    @Test
    void toDomain_mapsAllFields() {
        CtaDetourRouteDirection rtDir = new CtaDetourRouteDirection("8", "Northbound");
        CtaDetour wire = new CtaDetour(
            "1001", 2, 1,
            "Detour description",
            List.of(rtDir),
            "20200308 10:00:00",
            "20200308 18:00:00",
            null
        );

        Detour detour = DetourMapper.INSTANCE.toDomain(wire);

        assertThat(detour.id()).isEqualTo("1001");
        assertThat(detour.version()).isEqualTo("2");
        assertThat(detour.active()).isTrue();
        assertThat(detour.description()).isEqualTo("Detour description");
        assertThat(detour.routeDirections()).hasSize(1);
        assertThat(detour.routeDirections().getFirst().routeId()).isEqualTo("8");
        assertThat(detour.routeDirections().getFirst().direction()).isEqualTo("Northbound");
        assertThat(detour.dataFeed()).isNull();
    }

    @Test
    void toDomain_mapsInactive() {
        CtaDetour wire = new CtaDetour(
            "1001", 2, 0,
            "Detour description",
            List.of(),
            "20200308 10:00:00",
            "20200308 18:00:00",
            null
        );

        Detour detour = DetourMapper.INSTANCE.toDomain(wire);

        assertThat(detour.active()).isFalse();
    }
}
