package com.cta4j.bus.pattern.internal.wire;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CtaPatternTest {
    @Test
    void constructor_copiesDetourPoints_whenNonNull() {
        CtaPoint point = new CtaPoint(1, "S", "456", "Ashland & Division", 100.0f, 41.9, -87.67);
        List<CtaPoint> detourPoints = new ArrayList<>(List.of(point));

        CtaPattern pattern = new CtaPattern(3630, 14560, "Northbound", List.of(point), "12", detourPoints);
        detourPoints.add(point);

        assertThat(pattern.dtrpt()).containsExactly(point);
    }
}
