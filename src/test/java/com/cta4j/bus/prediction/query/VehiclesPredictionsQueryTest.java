package com.cta4j.bus.prediction.query;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VehiclesPredictionsQueryTest {
    @Test
    void builder_buildsQueryWithMaxResults() {
        VehiclesPredictionsQuery query = VehiclesPredictionsQuery.builder(List.of("509"))
            .maxResults(3)
            .build();

        assertThat(query.vehicleIds()).containsExactly("509");
        assertThat(query.maxResults()).isEqualTo(3);
    }

    @Test
    void builder_buildsQueryWithNoOptionalParams() {
        VehiclesPredictionsQuery query = VehiclesPredictionsQuery.builder(List.of("509")).build();

        assertThat(query.vehicleIds()).containsExactly("509");
        assertThat(query.maxResults()).isNull();
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsZero() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            VehiclesPredictionsQuery.builder(List.of("509")).maxResults(0));
    }

    @Test
    void builder_throwsIllegalArgumentException_whenMaxResultsIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            VehiclesPredictionsQuery.builder(List.of("509")).maxResults(-1));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenTooManyVehicleIds() {
        List<String> ids = Collections.nCopies(11, "509");

        assertThatIllegalArgumentException().isThrownBy(() ->
            new VehiclesPredictionsQuery(ids, null));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenMaxResultsIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new VehiclesPredictionsQuery(List.of("509"), 0));
    }
}
