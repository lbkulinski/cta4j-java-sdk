package com.cta4j.train.arrival.query;

import com.cta4j.train.common.model.TrainLine;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class StopArrivalsQueryTest {
    @Test
    void builder_buildsQueryWithOptionalParams() {
        StopArrivalsQuery query = StopArrivalsQuery.builder(List.of("30070"))
                                                   .line(TrainLine.RED)
                                                   .maxResults(5)
                                                   .build();

        assertThat(query.stopIds()).containsExactly("30070");
        assertThat(query.line()).isEqualTo(TrainLine.RED);
        assertThat(query.maxResults()).isEqualTo(5);
    }

    @Test
    void builder_buildsQueryWithMultipleStopIds() {
        StopArrivalsQuery query = StopArrivalsQuery.builder(List.of("30070", "30071", "30375")).build();

        assertThat(query.stopIds()).containsExactly("30070", "30071", "30375");
    }

    @Test
    void builder_buildsQueryWithNoOptionalParams() {
        StopArrivalsQuery query = StopArrivalsQuery.builder(List.of("30070")).build();

        assertThat(query.stopIds()).containsExactly("30070");
        assertThat(query.line()).isNull();
        assertThat(query.maxResults()).isNull();
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsZero() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            StopArrivalsQuery.builder(List.of("30070")).maxResults(0));
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            StopArrivalsQuery.builder(List.of("30070")).maxResults(-1));
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMoreThanFourStopIds() {
        List<String> stopIds = Collections.nCopies(5, "30070");

        assertThatIllegalArgumentException().isThrownBy(() ->
            StopArrivalsQuery.builder(stopIds).build());
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenMaxResultsIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new StopArrivalsQuery(List.of("30070"), null, 0));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenMoreThanFourStopIds() {
        List<String> stopIds = Collections.nCopies(5, "30070");

        assertThatIllegalArgumentException().isThrownBy(() ->
            new StopArrivalsQuery(stopIds, null, null));
    }

    @Test
    void constructor_allowsExactlyFourStopIds() {
        List<String> stopIds = Collections.nCopies(4, "30070");

        StopArrivalsQuery query = new StopArrivalsQuery(stopIds, null, null);

        assertThat(query.stopIds()).hasSize(4);
    }
}
