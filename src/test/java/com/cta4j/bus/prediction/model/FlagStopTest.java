package com.cta4j.bus.prediction.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FlagStopTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(FlagStop.fromCode(-1)).isEqualTo(FlagStop.UNDEFINED);
        assertThat(FlagStop.fromCode(0)).isEqualTo(FlagStop.NORMAL);
        assertThat(FlagStop.fromCode(1)).isEqualTo(FlagStop.PICKUP_AND_DISCHARGE);
        assertThat(FlagStop.fromCode(2)).isEqualTo(FlagStop.ONLY_DISCHARGE);
    }

    @Test
    void fromCode_throwsIllegalArgumentException_whenCodeIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> FlagStop.fromCode(99));
    }

    @Test
    void getCode_returnsCode() {
        assertThat(FlagStop.UNDEFINED.getCode()).isEqualTo(-1);
        assertThat(FlagStop.NORMAL.getCode()).isEqualTo(0);
    }
}