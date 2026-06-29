package com.cta4j.train.common.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TrainDirectionTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(TrainDirection.fromCode(1)).isEqualTo(TrainDirection.NORTHBOUND);
        assertThat(TrainDirection.fromCode(5)).isEqualTo(TrainDirection.SOUTHBOUND);
    }

    @Test
    void fromCode_throwsIllegalArgumentException_whenCodeIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> TrainDirection.fromCode(99));
    }

    @Test
    void getCode_returnsCode() {
        assertThat(TrainDirection.NORTHBOUND.getCode()).isEqualTo(1);
        assertThat(TrainDirection.SOUTHBOUND.getCode()).isEqualTo(5);
    }
}