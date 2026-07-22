package com.cta4j.train.arrival.query;

import com.cta4j.common.train.TrainLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StopArrivalsQueryTest {
    @Test
    void builder_buildsQueryWithOptionalParams() {
        StopArrivalsQuery query = StopArrivalsQuery.builder("30070")
                                                   .line(TrainLine.RED)
                                                   .maxResults(5)
                                                   .build();

        assertThat(query.stopId()).isEqualTo("30070");
        assertThat(query.line()).isEqualTo(TrainLine.RED);
        assertThat(query.maxResults()).isEqualTo(5);
    }

    @Test
    void builder_buildsQueryWithNoOptionalParams() {
        StopArrivalsQuery query = StopArrivalsQuery.builder("30070").build();

        assertThat(query.stopId()).isEqualTo("30070");
        assertThat(query.line()).isNull();
        assertThat(query.maxResults()).isNull();
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsZero() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            StopArrivalsQuery.builder("30070").maxResults(0));
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            StopArrivalsQuery.builder("30070").maxResults(-1));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenMaxResultsIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new StopArrivalsQuery("30070", null, 0));
    }
}
