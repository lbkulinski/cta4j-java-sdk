package com.cta4j.bus.vehicle.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TransitModeTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(TransitMode.fromCode(0)).isEqualTo(TransitMode.NONE);
        assertThat(TransitMode.fromCode(1)).isEqualTo(TransitMode.BUS);
        assertThat(TransitMode.fromCode(2)).isEqualTo(TransitMode.FERRY);
        assertThat(TransitMode.fromCode(3)).isEqualTo(TransitMode.RAIL);
        assertThat(TransitMode.fromCode(4)).isEqualTo(TransitMode.PEOPLE_MOVER);
    }

    @Test
    void fromCode_throwsIllegalArgumentException_whenCodeIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> TransitMode.fromCode(99));
    }

    @Test
    void getCode_returnsCode() {
        assertThat(TransitMode.NONE.getCode()).isEqualTo(0);
        assertThat(TransitMode.BUS.getCode()).isEqualTo(1);
    }
}
