package com.cta4j.bus.pattern;

import com.cta4j.bus.pattern.internal.mapper.RoutePatternMapper;
import com.cta4j.bus.pattern.internal.wire.CtaPattern;
import com.cta4j.bus.pattern.internal.wire.CtaPoint;
import com.cta4j.bus.pattern.model.PatternPoint;
import com.cta4j.bus.pattern.model.PatternPointType;
import com.cta4j.bus.pattern.model.RoutePattern;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RoutePatternMapperTest {
    @Test
    void toDomain_mapsAllFields() {
        CtaPoint point = new CtaPoint(1, "S", "456", "Ashland & Division", 100.0f, 41.9, -87.67);
        CtaPattern wire = new CtaPattern(3630, 14560, "Northbound", List.of(point), null, null);

        RoutePattern pattern = RoutePatternMapper.INSTANCE.toDomain(wire);

        assertThat(pattern.id()).isEqualTo("3630");
        assertThat(pattern.length()).isEqualTo(14560);
        assertThat(pattern.direction()).isEqualTo("Northbound");
        assertThat(pattern.points()).hasSize(1);
        assertThat(pattern.detourId()).isNull();
    }

    @Test
    void toDomain_mapsPoint() {
        CtaPoint pointWire = new CtaPoint(1, "S", "456", "Ashland & Division", 100.0f, 41.9, -87.67);
        CtaPattern wire = new CtaPattern(3630, 14560, "Northbound", List.of(pointWire), null, null);

        RoutePattern pattern = RoutePatternMapper.INSTANCE.toDomain(wire);
        PatternPoint point = pattern.points().getFirst();

        assertThat(point.sequence()).isEqualTo(1);
        assertThat(point.type()).isEqualTo(PatternPointType.STOP);
        assertThat(point.stopId()).isEqualTo("456");
        assertThat(point.latitude()).isEqualByComparingTo("41.9");
    }
}
