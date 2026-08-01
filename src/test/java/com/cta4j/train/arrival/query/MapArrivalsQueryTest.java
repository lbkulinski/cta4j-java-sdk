package com.cta4j.train.arrival.query;

import com.cta4j.train.common.model.TrainLine;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MapArrivalsQueryTest {
    @Test
    void builder_buildsQueryWithOptionalParams() {
        MapArrivalsQuery query = MapArrivalsQuery.builder(List.of("40900"))
                                                 .line(TrainLine.RED)
                                                 .maxResults(5)
                                                 .build();

        assertThat(query.mapIds()).containsExactly("40900");
        assertThat(query.line()).isEqualTo(TrainLine.RED);
        assertThat(query.maxResults()).isEqualTo(5);
    }

    @Test
    void builder_buildsQueryWithMultipleMapIds() {
        MapArrivalsQuery query = MapArrivalsQuery.builder(List.of("40900", "40380", "40360")).build();

        assertThat(query.mapIds()).containsExactly("40900", "40380", "40360");
    }

    @Test
    void builder_buildsQueryWithNoOptionalParams() {
        MapArrivalsQuery query = MapArrivalsQuery.builder(List.of("40900")).build();

        assertThat(query.mapIds()).containsExactly("40900");
        assertThat(query.line()).isNull();
        assertThat(query.maxResults()).isNull();
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsZero() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            MapArrivalsQuery.builder(List.of("40900")).maxResults(0));
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            MapArrivalsQuery.builder(List.of("40900")).maxResults(-1));
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMoreThanFourMapIds() {
        List<String> mapIds = Collections.nCopies(5, "40900");

        assertThatIllegalArgumentException().isThrownBy(() ->
            MapArrivalsQuery.builder(mapIds).build());
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenMaxResultsIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new MapArrivalsQuery(List.of("40900"), null, 0));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenMoreThanFourMapIds() {
        List<String> mapIds = Collections.nCopies(5, "40900");

        assertThatIllegalArgumentException().isThrownBy(() ->
            new MapArrivalsQuery(mapIds, null, null));
    }

    @Test
    void constructor_allowsExactlyFourMapIds() {
        List<String> mapIds = Collections.nCopies(4, "40900");

        MapArrivalsQuery query = new MapArrivalsQuery(mapIds, null, null);

        assertThat(query.mapIds()).hasSize(4);
    }
}
