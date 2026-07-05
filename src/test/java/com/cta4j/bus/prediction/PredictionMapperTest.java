package com.cta4j.bus.prediction;

import com.cta4j.bus.prediction.internal.mapper.PredictionMapper;
import com.cta4j.bus.prediction.internal.wire.CtaPrediction;
import com.cta4j.bus.prediction.model.Prediction;
import com.cta4j.bus.prediction.model.PredictionType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PredictionMapperTest {
    @Test
    void toDomain_mapsAllFields() {
        CtaPrediction wire = new CtaPrediction(
            "20200308 10:28:00",
            "A",
            "456", "Ashland & Division",
            509, 1000,
            "8", "8", "Northbound",
            "Waveland/Broadway",
            "20200308 10:30:00",
            false,
            0,
            "block1", "trip1", "original1",
            null, "", "EMPTY",
            null, null, null, null,
            0
        );

        Prediction prediction = PredictionMapper.INSTANCE.toDomain(wire);

        assertThat(prediction.stopId()).isEqualTo("456");
        assertThat(prediction.stopName()).isEqualTo("Ashland & Division");
        assertThat(prediction.vehicleId()).isEqualTo("509");
        assertThat(prediction.routeId()).isEqualTo("8");
        assertThat(prediction.destination()).isEqualTo("Waveland/Broadway");
        assertThat(prediction.delayed()).isFalse();
        assertThat(prediction.predictionType()).isEqualTo(PredictionType.ARRIVAL);
    }

    @Test
    void toDomain_mapsDepartureType() {
        CtaPrediction wire = new CtaPrediction(
            "20200308 10:28:00",
            "D",
            "456", "Ashland & Division",
            509, 1000,
            "8", "8", "Northbound",
            "Waveland/Broadway",
            "20200308 10:30:00",
            false,
            0,
            "block1", "trip1", "original1",
            null, "", "EMPTY",
            null, null, null, null,
            0
        );

        Prediction prediction = PredictionMapper.INSTANCE.toDomain(wire);

        assertThat(prediction.predictionType()).isEqualTo(PredictionType.DEPARTURE);
    }
}
