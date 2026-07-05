package com.cta4j.train.follow;

import com.cta4j.train.common.internal.wire.CtaArrival;
import com.cta4j.train.follow.internal.mapper.FollowTrainMapper;
import com.cta4j.train.follow.internal.wire.CtaFollowResponse;
import com.cta4j.train.follow.internal.wire.CtaPosition;
import com.cta4j.train.follow.model.FollowTrain;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FollowTrainMapperTest {
    @Test
    void toDomain_mapsArrivalsAndCoordinates() {
        CtaPosition position = new CtaPosition("41.88", "-87.63", "180");
        CtaArrival arrival = new CtaArrival(
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
        CtaFollowResponse wire = new CtaFollowResponse(
            "2015-04-30T20:23:53", "0", null,
            position, List.of(arrival)
        );

        FollowTrain followTrain = FollowTrainMapper.INSTANCE.toDomain(wire);

        assertThat(followTrain.arrivals()).hasSize(1);
        assertThat(followTrain.coordinates()).isNotNull();
        assertThat(followTrain.coordinates().latitude()).isEqualByComparingTo("41.88");
        assertThat(followTrain.coordinates().longitude()).isEqualByComparingTo("-87.63");
    }
}
