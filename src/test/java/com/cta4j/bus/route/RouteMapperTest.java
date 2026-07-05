package com.cta4j.bus.route;

import com.cta4j.bus.route.internal.mapper.RouteMapper;
import com.cta4j.bus.route.internal.wire.CtaRoute;
import com.cta4j.bus.route.model.Route;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RouteMapperTest {
    @Test
    void toDomain_mapsAllFields() {
        CtaRoute wire = new CtaRoute("22", "Clark", "#ffffff", "22", "feed1");

        Route route = RouteMapper.INSTANCE.toDomain(wire);

        assertThat(route.id()).isEqualTo("22");
        assertThat(route.name()).isEqualTo("Clark");
        assertThat(route.color()).isEqualTo("#ffffff");
        assertThat(route.designator()).isEqualTo("22");
        assertThat(route.dataFeed()).isEqualTo("feed1");
    }

    @Test
    void toDomain_mapsNullDataFeed() {
        CtaRoute wire = new CtaRoute("22", "Clark", "#ffffff", "22", null);

        Route route = RouteMapper.INSTANCE.toDomain(wire);

        assertThat(route.dataFeed()).isNull();
    }
}
