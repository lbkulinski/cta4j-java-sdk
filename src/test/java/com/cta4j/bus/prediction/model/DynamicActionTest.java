package com.cta4j.bus.prediction.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DynamicActionTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(DynamicAction.fromCode(0)).isEqualTo(DynamicAction.NONE);
        assertThat(DynamicAction.fromCode(1)).isEqualTo(DynamicAction.CANCELLED);
        assertThat(DynamicAction.fromCode(2)).isEqualTo(DynamicAction.REASSIGNED);
        assertThat(DynamicAction.fromCode(3)).isEqualTo(DynamicAction.SHIFTED);
        assertThat(DynamicAction.fromCode(4)).isEqualTo(DynamicAction.EXPRESSED);
        assertThat(DynamicAction.fromCode(6)).isEqualTo(DynamicAction.STOPS_AFFECTED);
        assertThat(DynamicAction.fromCode(8)).isEqualTo(DynamicAction.NEW_TRIP);
        assertThat(DynamicAction.fromCode(9)).isEqualTo(DynamicAction.PARTIAL_TRIP);
        assertThat(DynamicAction.fromCode(10)).isEqualTo(DynamicAction.PARTIAL_TRIP_NEW);
        assertThat(DynamicAction.fromCode(12)).isEqualTo(DynamicAction.DELAYED_CANCEL);
        assertThat(DynamicAction.fromCode(13)).isEqualTo(DynamicAction.ADDED_STOP);
        assertThat(DynamicAction.fromCode(14)).isEqualTo(DynamicAction.UNKNOWN_DELAY);
        assertThat(DynamicAction.fromCode(15)).isEqualTo(DynamicAction.UNKNOWN_DELAY_NEW);
        assertThat(DynamicAction.fromCode(16)).isEqualTo(DynamicAction.INVALIDATED_TRIP);
        assertThat(DynamicAction.fromCode(17)).isEqualTo(DynamicAction.INVALIDATED_TRIP_NEW);
        assertThat(DynamicAction.fromCode(18)).isEqualTo(DynamicAction.CANCELLED_TRIP_NEW);
        assertThat(DynamicAction.fromCode(19)).isEqualTo(DynamicAction.STOPS_AFFECTED_NEW);
    }

    @Test
    void fromCode_throwsIllegalArgumentException_whenCodeIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> DynamicAction.fromCode(99));
    }

    @Test
    void getCode_returnsCode() {
        assertThat(DynamicAction.NONE.getCode()).isEqualTo(0);
        assertThat(DynamicAction.CANCELLED.getCode()).isEqualTo(1);
    }
}
