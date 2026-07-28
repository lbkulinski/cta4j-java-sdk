package com.cta4j.train.location;

import com.cta4j.train.common.model.TrainDirection;
import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.location.internal.mapper.TrainLocationsMapper;
import com.cta4j.train.location.internal.wire.CtaLocationTrain;
import com.cta4j.train.location.internal.wire.CtaRoute;
import com.cta4j.train.location.model.LocationTrain;
import com.cta4j.train.location.model.TrainLocations;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TrainLocationsMapperTest {
    @Test
    void toDomain_mapsLineAndTrains() {
        CtaLocationTrain train = new CtaLocationTrain(
            "123", "30077", "O'Hare", "1",
            "40100", "30070", "Howard",
            "2015-04-30T20:23:53",
            "2015-04-30T20:25:00",
            "0", "0", null,
            "41.88", "-87.63", "180"
        );
        CtaRoute wire = new CtaRoute("Red", List.of(train));

        TrainLocations locations = TrainLocationsMapper.INSTANCE.toDomain(wire);

        assertThat(locations.line()).isEqualTo(TrainLine.RED);
        assertThat(locations.trains()).hasSize(1);
        assertThat(locations.trains().getFirst().run()).isEqualTo("123");
        assertThat(locations.trains().getFirst().destinationName()).isEqualTo("O'Hare");
    }

    @Test
    void toDomain_mapsNullTrainListAsEmpty() {
        CtaRoute wire = new CtaRoute("Red", null);

        TrainLocations locations = TrainLocationsMapper.INSTANCE.toDomain(wire);

        assertThat(locations.trains()).isEmpty();
    }

    @Test
    void toDomain_train_mapsAllFields() {
        CtaLocationTrain wire = new CtaLocationTrain(
            "123", "30077", "O'Hare", "1",
            "40100", "30070", "Howard",
            "2015-04-30T20:23:53",
            "2015-04-30T20:25:00",
            "0", "0", "some-flag",
            "41.88", "-87.63", "180"
        );

        LocationTrain train = TrainLocationsMapper.INSTANCE.toDomain(wire);

        assertThat(train.run()).isEqualTo("123");
        assertThat(train.destinationStationId()).isEqualTo("30077");
        assertThat(train.destinationName()).isEqualTo("O'Hare");
        assertThat(train.direction()).isEqualTo(TrainDirection.NORTHBOUND);
        assertThat(train.nextStationId()).isEqualTo("40100");
        assertThat(train.nextStopId()).isEqualTo("30070");
        assertThat(train.nextStationName()).isEqualTo("Howard");
        assertThat(train.approaching()).isFalse();
        assertThat(train.delayed()).isFalse();
        assertThat(train.flags()).isEqualTo("some-flag");
        assertThat(train.coordinates()).isNotNull();
        assertThat(train.coordinates().latitude()).isEqualByComparingTo("41.88");
        assertThat(train.coordinates().longitude()).isEqualByComparingTo("-87.63");
        assertThat(train.coordinates().heading()).isEqualTo(180);
    }

    @Test
    void toDomain_train_mapsSouthboundDirection() {
        CtaLocationTrain wire = new CtaLocationTrain(
            "123", "30077", "O'Hare", "5",
            "40100", "30070", "Howard",
            "2015-04-30T20:23:53",
            "2015-04-30T20:25:00",
            "0", "0", null,
            "41.88", "-87.63", "180"
        );

        LocationTrain train = TrainLocationsMapper.INSTANCE.toDomain(wire);

        assertThat(train.direction()).isEqualTo(TrainDirection.SOUTHBOUND);
    }

    @Test
    void toDomain_train_mapsApproachingAndDelayedTrue() {
        CtaLocationTrain wire = new CtaLocationTrain(
            "123", "30077", "O'Hare", "1",
            "40100", "30070", "Howard",
            "2015-04-30T20:23:53",
            "2015-04-30T20:25:00",
            "1", "1", null,
            "41.88", "-87.63", "180"
        );

        LocationTrain train = TrainLocationsMapper.INSTANCE.toDomain(wire);

        assertThat(train.approaching()).isTrue();
        assertThat(train.delayed()).isTrue();
    }

    @Test
    void toDomain_train_mapsNullFlagsAsNull() {
        CtaLocationTrain wire = new CtaLocationTrain(
            "123", "30077", "O'Hare", "1",
            "40100", "30070", "Howard",
            "2015-04-30T20:23:53",
            "2015-04-30T20:25:00",
            "0", "0", null,
            "41.88", "-87.63", "180"
        );

        LocationTrain train = TrainLocationsMapper.INSTANCE.toDomain(wire);

        assertThat(train.flags()).isNull();
    }
}
