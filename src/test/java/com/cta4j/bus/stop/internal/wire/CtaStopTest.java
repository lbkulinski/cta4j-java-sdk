package com.cta4j.bus.stop.internal.wire;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CtaStopTest {
    @Test
    void constructor_copiesDetoursAdded_whenNonNull() {
        List<Integer> detoursAdded = new ArrayList<>(List.of(123, 456));

        CtaStop stop = new CtaStop("456", "Ashland & Division", 41.9, -87.67, detoursAdded, null, null, null);
        detoursAdded.add(789);

        assertThat(stop.dtradd()).containsExactly(123, 456);
    }

    @Test
    void constructor_copiesDetoursRemoved_whenNonNull() {
        List<Integer> detoursRemoved = new ArrayList<>(List.of(321, 654));

        CtaStop stop = new CtaStop("456", "Ashland & Division", 41.9, -87.67, null, detoursRemoved, null, null);
        detoursRemoved.add(987);

        assertThat(stop.dtrrem()).containsExactly(321, 654);
    }
}