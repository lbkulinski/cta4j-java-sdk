package com.cta4j.alert.detailedalert.query;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class AlertsQueryTest {
    @Test
    void builder_hasCtaDefaults() {
        AlertsQuery query = AlertsQuery.builder().build();

        assertThat(query.activeOnly()).isFalse();
        assertThat(query.accessibility()).isTrue();
        assertThat(query.planned()).isTrue();
        assertThat(query.byStartDate()).isNull();
        assertThat(query.recentDays()).isNull();
    }

    @Test
    void builder_buildsQueryWithOptionalParams() {
        LocalDate date = LocalDate.of(2026, 7, 1);

        AlertsQuery query = AlertsQuery.builder()
                                       .activeOnly(true)
                                       .accessibility(false)
                                       .planned(false)
                                       .byStartDate(date)
                                       .build();

        assertThat(query.activeOnly()).isTrue();
        assertThat(query.accessibility()).isFalse();
        assertThat(query.planned()).isFalse();
        assertThat(query.byStartDate()).isEqualTo(date);
        assertThat(query.recentDays()).isNull();
    }

    @Test
    void builder_buildsQueryWithRecentDays() {
        AlertsQuery query = AlertsQuery.builder().recentDays(7).build();

        assertThat(query.recentDays()).isEqualTo(7);
        assertThat(query.byStartDate()).isNull();
    }

    @Test
    void builder_recentDays_throwsIllegalArgumentException_whenZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> AlertsQuery.builder().recentDays(0));
    }

    @Test
    void builder_recentDays_throwsIllegalArgumentException_whenNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> AlertsQuery.builder().recentDays(-1));
    }

    @Test
    void builder_byStartDate_throwsNullPointerException_whenNull() {
        assertThatNullPointerException().isThrownBy(() -> AlertsQuery.builder().byStartDate(null));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenByStartDateAndRecentDaysBothSpecified() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new AlertsQuery(false, true, true, LocalDate.of(2026, 7, 1), 7));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenRecentDaysIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new AlertsQuery(false, true, true, null, 0));
    }
}
