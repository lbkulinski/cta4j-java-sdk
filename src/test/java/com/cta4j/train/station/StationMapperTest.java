package com.cta4j.train.station;

import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.station.internal.mapper.StationMapper;
import com.cta4j.train.station.internal.wire.CtaLocation;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.CardinalDirection;
import com.cta4j.train.station.model.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StationMapperTest {
    @Test
    void toDomain_mapsAllFields() {
        CtaLocation location = new CtaLocation("42.019063", "-87.672892", null);
        CtaStation wire = new CtaStation(
            "30070", "N",
            "Howard (NB/SB)", "Howard",
            "Howard (Red/Yellow/Purple Lines)",
            "40900",
            true,
            true, false, false, false, true, true, false, false,
            location
        );

        Station station = StationMapper.INSTANCE.toDomain(wire);

        assertThat(station.stopId()).isEqualTo("30070");
        assertThat(station.direction()).isEqualTo(CardinalDirection.NORTH);
        assertThat(station.name()).isEqualTo("Howard");
        assertThat(station.mapId()).isEqualTo("40900");
        assertThat(station.adaAccessible()).isTrue();
        assertThat(station.lines()).contains(TrainLine.RED);
        assertThat(station.lines()).contains(TrainLine.PURPLE);
        assertThat(station.lines()).contains(TrainLine.YELLOW);
        assertThat(station.lines()).doesNotContain(TrainLine.BLUE);
    }
}
