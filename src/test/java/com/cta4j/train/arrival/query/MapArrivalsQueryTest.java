package com.cta4j.train.arrival.query;

import com.cta4j.common.train.TrainLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MapArrivalsQueryTest {
    @Test
    void builder_buildsQueryWithOptionalParams() {
        MapArrivalsQuery query = MapArrivalsQuery.builder("40900")
                                                 .line(TrainLine.RED)
                                                 .maxResults(5)
                                                 .build();

        assertThat(query.mapId()).isEqualTo("40900");
        assertThat(query.line()).isEqualTo(TrainLine.RED);
        assertThat(query.maxResults()).isEqualTo(5);
    }

    @Test
    void builder_buildsQueryWithNoOptionalParams() {
        MapArrivalsQuery query = MapArrivalsQuery.builder("40900").build();

        assertThat(query.mapId()).isEqualTo("40900");
        assertThat(query.line()).isNull();
        assertThat(query.maxResults()).isNull();
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsZero() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            MapArrivalsQuery.builder("40900").maxResults(0));
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            MapArrivalsQuery.builder("40900").maxResults(-1));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenMaxResultsIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new MapArrivalsQuery("40900", null, 0));
    }
}
