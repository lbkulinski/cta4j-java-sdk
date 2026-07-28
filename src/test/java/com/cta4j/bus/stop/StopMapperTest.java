package com.cta4j.bus.stop;

import com.cta4j.bus.stop.internal.mapper.StopMapper;
import com.cta4j.bus.stop.internal.wire.CtaStop;
import com.cta4j.bus.stop.model.Stop;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class StopMapperTest {
    @Test
    void toDomain_mapsRequiredFields() {
        CtaStop wire = new CtaStop("456", "Ashland & Division", 41.9, -87.67, null, null, null, null);

        Stop stop = StopMapper.INSTANCE.toDomain(wire);

        assertThat(stop.id()).isEqualTo("456");
        assertThat(stop.name()).isEqualTo("Ashland & Division");
        assertThat(stop.latitude()).isEqualByComparingTo("41.9");
        assertThat(stop.longitude()).isEqualByComparingTo("-87.67");
    }

    @Test
    void toDomain_mapsNullAdaAsNull() {
        CtaStop wire = new CtaStop("456", "Ashland & Division", 41.9, -87.67, null, null, null, null);

        Stop stop = StopMapper.INSTANCE.toDomain(wire);

        assertThat(stop.adaAccessible()).isNull();
    }

    @Test
    void toDomain_mapsAdaAccessibleTrue() {
        CtaStop wire = new CtaStop("456", "Ashland & Division", 41.9, -87.67, null, null, null, true);

        Stop stop = StopMapper.INSTANCE.toDomain(wire);

        assertThat(stop.adaAccessible()).isTrue();
    }

    @Test
    void toDomain_mapsDetoursAddedAndRemoved_whenNonNull() {
        CtaStop wire = new CtaStop(
            "456",
            "Ashland & Division",
            41.9,
            -87.67,
            List.of(1, 2),
            List.of(3),
            5,
            null
        );

        Stop stop = StopMapper.INSTANCE.toDomain(wire);

        assertThat(stop.detoursAdded()).containsExactly(1, 2);
        assertThat(stop.detoursRemoved()).containsExactly(3);
        assertThat(stop.gtfsSequence()).isEqualTo(5);
    }

    @Test
    void toDomain_returnsNull_whenStopIsNull() {
        Stop stop = StopMapper.INSTANCE.toDomain(null);

        assertThat(stop).isNull();
    }
}
