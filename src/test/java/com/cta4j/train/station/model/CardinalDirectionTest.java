package com.cta4j.train.station.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CardinalDirectionTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(CardinalDirection.fromCode("N")).isEqualTo(CardinalDirection.NORTH);
        assertThat(CardinalDirection.fromCode("North")).isEqualTo(CardinalDirection.NORTH);
        assertThat(CardinalDirection.fromCode("E")).isEqualTo(CardinalDirection.EAST);
        assertThat(CardinalDirection.fromCode("East")).isEqualTo(CardinalDirection.EAST);
        assertThat(CardinalDirection.fromCode("S")).isEqualTo(CardinalDirection.SOUTH);
        assertThat(CardinalDirection.fromCode("South")).isEqualTo(CardinalDirection.SOUTH);
        assertThat(CardinalDirection.fromCode("W")).isEqualTo(CardinalDirection.WEST);
        assertThat(CardinalDirection.fromCode("West")).isEqualTo(CardinalDirection.WEST);
    }

    @Test
    void fromCode_throwsIllegalArgumentException_whenCodeIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> CardinalDirection.fromCode("X"));
    }
}