package com.cta4j.bus.stop.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class StopTest {
    @Test
    void constructor_copiesDetoursAdded_whenNonNull() {
        List<Integer> detoursAdded = new ArrayList<>(List.of(123, 456));

        Stop stop = new Stop(
            "456", "Ashland & Division", new BigDecimal("41.9"), new BigDecimal("-87.67"),
            detoursAdded, null, null, null
        );
        detoursAdded.add(789);

        assertThat(stop.detoursAdded()).containsExactly(123, 456);
    }

    @Test
    void constructor_copiesDetoursRemoved_whenNonNull() {
        List<Integer> detoursRemoved = new ArrayList<>(List.of(321, 654));

        Stop stop = new Stop(
            "456", "Ashland & Division", new BigDecimal("41.9"), new BigDecimal("-87.67"),
            null, detoursRemoved, null, null
        );
        detoursRemoved.add(987);

        assertThat(stop.detoursRemoved()).containsExactly(321, 654);
    }
}
