package com.cta4j.alert.detailedalert.query;

import com.cta4j.alert.common.model.AlertTrainLine;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LineAlertsQueryTest {
    @Test
    void builder_copiesLines_andHasCtaDefaults() {
        List<AlertTrainLine> lines = new ArrayList<>(List.of(AlertTrainLine.RED, AlertTrainLine.BLUE));

        LineAlertsQuery query = LineAlertsQuery.builder(lines).build();
        lines.add(AlertTrainLine.GREEN);

        assertThat(query.lines()).containsExactly(AlertTrainLine.RED, AlertTrainLine.BLUE);
        assertThat(query.activeOnly()).isFalse();
        assertThat(query.accessibility()).isTrue();
        assertThat(query.planned()).isTrue();
    }

    @Test
    void builder_buildsQueryWithOptionalParams() {
        LineAlertsQuery query = LineAlertsQuery.builder(List.of(AlertTrainLine.RED))
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

        LineAlertsQuery query = LineAlertsQuery.builder(List.of(AlertTrainLine.RED))
                                               .byStartDate(date)
                                               .build();

        assertThat(query.byStartDate()).isEqualTo(date);
        assertThat(query.recentDays()).isNull();
    }

    @Test
    void builder_recentDays_throwsIllegalArgumentException_whenNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            LineAlertsQuery.builder(List.of(AlertTrainLine.RED)).recentDays(0));
    }

    @Test
    void builder_throwsNullPointerException_whenLinesIsNull() {
        assertThatNullPointerException().isThrownBy(() -> LineAlertsQuery.builder(null));
    }

    @Test
    void constructor_throwsNullPointerException_whenLinesIsNull() {
        assertThatNullPointerException().isThrownBy(() ->
            new LineAlertsQuery(null, false, true, true, null, null));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenByStartDateAndRecentDaysBothSpecified() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new LineAlertsQuery(List.of(AlertTrainLine.RED), false, true, true, LocalDate.of(2026, 7, 1), 7));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenRecentDaysIsNotPositive() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new LineAlertsQuery(List.of(AlertTrainLine.RED), false, true, true, null, -1));
    }
}
