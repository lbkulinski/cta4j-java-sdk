package com.cta4j.bus.prediction.query;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class StopsPredictionsQueryTest {
    @Test
    void builder_buildsQueryWithOptionalParams() {
        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("1001"))
            .routeIds(List.of("22", "36"))
            .maxResults(5)
            .build();

        assertThat(query.stopIds()).containsExactly("1001");
        assertThat(query.routeIds()).containsExactly("22", "36");
        assertThat(query.maxResults()).isEqualTo(5);
    }

    @Test
    void builder_buildsQueryWithNoOptionalParams() {
        StopsPredictionsQuery query = StopsPredictionsQuery.builder(List.of("1001")).build();

        assertThat(query.stopIds()).containsExactly("1001");
        assertThat(query.routeIds()).isNull();
        assertThat(query.maxResults()).isNull();
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsZero() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            StopsPredictionsQuery.builder(List.of("1001")).maxResults(0));
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            StopsPredictionsQuery.builder(List.of("1001")).maxResults(-1));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenTooManyStopIds() {
        List<String> ids = Collections.nCopies(11, "1001");

        assertThatIllegalArgumentException().isThrownBy(() ->
            new StopsPredictionsQuery(ids, null, null));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenMaxResultsIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new StopsPredictionsQuery(List.of("1001"), null, 0));
    }
}