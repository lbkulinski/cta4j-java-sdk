package com.cta4j.alert.detailedalert.query;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class StationAlertsQueryTest {
    @Test
    void builder_copiesStationIds_andHasCtaDefaults() {
        List<String> stationIds = new ArrayList<>(List.of("40380", "41260"));

        StationAlertsQuery query = StationAlertsQuery.builder(stationIds).build();
        stationIds.add("40560");

        assertThat(query.stationIds()).containsExactly("40380", "41260");
        assertThat(query.activeOnly()).isFalse();
        assertThat(query.accessibility()).isTrue();
        assertThat(query.planned()).isTrue();
    }

    @Test
    void builder_buildsQueryWithOptionalParams() {
        StationAlertsQuery query = StationAlertsQuery.builder(List.of("40380"))
                                                      .activeOnly(true)
                                                      .accessibility(false)
                                                      .planned(false)
                                                      .byStartDate(LocalDate.of(2026, 7, 1))
                                                      .build();

        assertThat(query.activeOnly()).isTrue();
        assertThat(query.accessibility()).isFalse();
        assertThat(query.planned()).isFalse();
        assertThat(query.byStartDate()).isEqualTo(LocalDate.of(2026, 7, 1));
    }

    @Test
    void builder_setsRecentDays() {
        StationAlertsQuery query = StationAlertsQuery.builder(List.of("40380"))
                                                      .recentDays(7)
                                                      .build();

        assertThat(query.recentDays()).isEqualTo(7);
        assertThat(query.byStartDate()).isNull();
    }

    @Test
    void builder_recentDays_throwsIllegalArgumentException_whenNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            StationAlertsQuery.builder(List.of("40380")).recentDays(0));
    }

    @Test
    void builder_throwsNullPointerException_whenStationIdsIsNull() {
        assertThatNullPointerException().isThrownBy(() -> StationAlertsQuery.builder(null));
    }

    @Test
    void constructor_throwsNullPointerException_whenStationIdsIsNull() {
        assertThatNullPointerException().isThrownBy(() ->
            new StationAlertsQuery(null, false, true, true, null, null));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenByStartDateAndRecentDaysBothSpecified() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new StationAlertsQuery(List.of("40380"), false, true, true, LocalDate.of(2026, 7, 1), 7));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenRecentDaysIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new StationAlertsQuery(List.of("40380"), false, true, true, null, -1));
    }
}