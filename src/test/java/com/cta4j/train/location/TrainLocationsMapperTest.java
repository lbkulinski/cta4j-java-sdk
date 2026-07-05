package com.cta4j.train.location;

import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.location.internal.mapper.TrainLocationsMapper;
import com.cta4j.train.location.internal.wire.CtaLocationTrain;
import com.cta4j.train.location.internal.wire.CtaRoute;
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
}
