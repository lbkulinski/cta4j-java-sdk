package com.cta4j.train.arrival;

import com.cta4j.train.common.internal.mapper.ArrivalMapper;
import com.cta4j.train.common.internal.wire.CtaArrival;
import com.cta4j.train.common.model.Arrival;
import com.cta4j.train.common.model.TrainLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArrivalMapperTest {
    @Test
    void toDomain_mapsAllFields() {
        CtaArrival wire = new CtaArrival(
            "40100", "30070",
            "Howard", "Service toward O'Hare",
            "123", "Red",
            "30077", "O'Hare",
            "1",
            "2015-04-30T20:23:53",
            "2015-04-30T20:25:00",
            "0", "0", "0", "0",
            null, null, null, null
        );

        Arrival arrival = ArrivalMapper.INSTANCE.toDomain(wire);

        assertThat(arrival.stationId()).isEqualTo("40100");
        assertThat(arrival.stationName()).isEqualTo("Howard");
        assertThat(arrival.stopId()).isEqualTo("30070");
        assertThat(arrival.stopDescription()).isEqualTo("Service toward O'Hare");
        assertThat(arrival.line()).isEqualTo(TrainLine.RED);
        assertThat(arrival.destinationStationId()).isEqualTo("30077");
        assertThat(arrival.destinationName()).isEqualTo("O'Hare");
        assertThat(arrival.approaching()).isFalse();
        assertThat(arrival.scheduled()).isFalse();
        assertThat(arrival.delayed()).isFalse();
        assertThat(arrival.fault()).isFalse();
    }

    @Test
    void toDomain_mapsDelayedTrue() {
        CtaArrival wire = new CtaArrival(
            "40100", "30070",
            "Howard", "Service toward O'Hare",
            "123", "Red",
            "30077", "O'Hare",
            "1",
            "2015-04-30T20:23:53",
            "2015-04-30T20:25:00",
            "0", "0", "1", "0",
            null, null, null, null
        );

        Arrival arrival = ArrivalMapper.INSTANCE.toDomain(wire);

        assertThat(arrival.delayed()).isTrue();
    }

    @Test
    void toDomain_mapsApproachingTrue() {
        CtaArrival wire = new CtaArrival(
            "40100", "30070",
            "Howard", "Service toward O'Hare",
            "123", "Red",
            "30077", "O'Hare",
            "1",
            "2015-04-30T20:23:53",
            "2015-04-30T20:25:00",
            "1", "0", "0", "0",
            null, null, null, null
        );

        Arrival arrival = ArrivalMapper.INSTANCE.toDomain(wire);

        assertThat(arrival.approaching()).isTrue();
    }
}
