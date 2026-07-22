package com.cta4j.bus.pattern.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RoutePatternTest {
    @Test
    void constructor_copiesDetourPoints_whenNonNull() {
        PatternPoint point = new PatternPoint(
            1, PatternPointType.STOP, "456", "Ashland & Division", null,
            new BigDecimal("41.9"), new BigDecimal("-87.67")
        );
        List<PatternPoint> detourPoints = new ArrayList<>(List.of(point));

        RoutePattern pattern = new RoutePattern("3630", 14560, "Northbound", List.of(point), "12", detourPoints);
        detourPoints.add(point);

        assertThat(pattern.detourPoints()).containsExactly(point);
    }
}
