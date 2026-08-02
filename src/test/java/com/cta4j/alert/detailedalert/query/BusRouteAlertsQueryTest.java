package com.cta4j.alert.detailedalert.query;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BusRouteAlertsQueryTest {
    @Test
    void builder_copiesRouteIds_andHasCtaDefaults() {
        List<String> routeIds = new ArrayList<>(List.of("22", "53"));

        BusRouteAlertsQuery query = BusRouteAlertsQuery.builder(routeIds).build();
        routeIds.add("9");

        assertThat(query.routeIds()).containsExactly("22", "53");
        assertThat(query.activeOnly()).isFalse();
        assertThat(query.accessibility()).isTrue();
        assertThat(query.planned()).isTrue();
    }

    @Test
    void builder_buildsQueryWithOptionalParams() {
        BusRouteAlertsQuery query = BusRouteAlertsQuery.builder(List.of("22"))
                                                       .activeOnly(true)
                                                       .accessibility(false)
                                                       .planned(false)
                                                       .recentDays(3)
                                                       .build();

        assertThat(query.activeOnly()).isTrue();
        assertThat(query.accessibility()).isFalse();
        assertThat(query.planned()).isFalse();
        assertThat(query.recentDays()).isEqualTo(3);
    }

    @Test
    void builder_setsByStartDate() {
        LocalDate date = LocalDate.of(2026, 7, 1);

        BusRouteAlertsQuery query = BusRouteAlertsQuery.builder(List.of("22"))
                                                       .byStartDate(date)
                                                       .build();

        assertThat(query.byStartDate()).isEqualTo(date);
        assertThat(query.recentDays()).isNull();
    }

    @Test
    void builder_throwsNullPointerException_whenRouteIdsIsNull() {
        assertThatNullPointerException().isThrownBy(() -> BusRouteAlertsQuery.builder(null));
    }

    @Test
    void builder_recentDays_throwsIllegalArgumentException_whenNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() -> BusRouteAlertsQuery.builder(List.of("22")).recentDays(0));
    }

    @Test
    void constructor_throwsNullPointerException_whenRouteIdsIsNull() {
        assertThatNullPointerException().isThrownBy(() ->
            new BusRouteAlertsQuery(null, false, true, true, null, null));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenByStartDateAndRecentDaysBothSpecified() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new BusRouteAlertsQuery(List.of("22"), false, true, true, LocalDate.of(2026, 7, 1), 7));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenRecentDaysIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new BusRouteAlertsQuery(List.of("22"), false, true, true, null, -1));
    }
}
