package com.cta4j.bus.prediction.model;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

class PredictionTest {
    private static Prediction prediction(Instant arrivalTime) {
        PredictionMetadata metadata = new PredictionMetadata(
            Instant.now(), DynamicAction.NONE, "1234", "5678", "5678",
            null, "", PassengerLoad.UNKNOWN, null, null, null, null, FlagStop.NORMAL
        );

        return new Prediction(
            PredictionType.ARRIVAL, "456", "Ashland & Division", "1234", BigInteger.valueOf(500),
            "8", "8", "Northbound", "Union Station", arrivalTime, false, metadata
        );
    }

    @Test
    void etaMinutes_returnsMinutesUntilArrival_whenArrivalTimeIsInFuture() {
        Instant arrivalTime = Instant.now().plus(10, ChronoUnit.MINUTES);

        long eta = prediction(arrivalTime).etaMinutes();

        assertThat(eta).isBetween(8L, 10L);
    }

    @Test
    void etaMinutes_returnsZero_whenArrivalTimeIsInPast() {
        Instant arrivalTime = Instant.now().minus(10, ChronoUnit.MINUTES);

        long eta = prediction(arrivalTime).etaMinutes();

        assertThat(eta).isZero();
    }
}