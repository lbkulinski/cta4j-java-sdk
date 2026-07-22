package com.cta4j.train.common.model;

import com.cta4j.common.train.TrainLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TrainLineTest {
    @Test
    void fromCode_returnsCorrectValues() {
        assertThat(TrainLine.fromCode("Red")).isEqualTo(TrainLine.RED);
        assertThat(TrainLine.fromCode("Red Line")).isEqualTo(TrainLine.RED);
        assertThat(TrainLine.fromCode("Blue")).isEqualTo(TrainLine.BLUE);
        assertThat(TrainLine.fromCode("Blue Line")).isEqualTo(TrainLine.BLUE);
        assertThat(TrainLine.fromCode("Brn")).isEqualTo(TrainLine.BROWN);
        assertThat(TrainLine.fromCode("Brown Line")).isEqualTo(TrainLine.BROWN);
        assertThat(TrainLine.fromCode("G")).isEqualTo(TrainLine.GREEN);
        assertThat(TrainLine.fromCode("Green Line")).isEqualTo(TrainLine.GREEN);
        assertThat(TrainLine.fromCode("Org")).isEqualTo(TrainLine.ORANGE);
        assertThat(TrainLine.fromCode("Orange Line")).isEqualTo(TrainLine.ORANGE);
        assertThat(TrainLine.fromCode("P")).isEqualTo(TrainLine.PURPLE);
        assertThat(TrainLine.fromCode("Purple Line")).isEqualTo(TrainLine.PURPLE);
        assertThat(TrainLine.fromCode("Pink")).isEqualTo(TrainLine.PINK);
        assertThat(TrainLine.fromCode("Pink Line")).isEqualTo(TrainLine.PINK);
        assertThat(TrainLine.fromCode("Y")).isEqualTo(TrainLine.YELLOW);
        assertThat(TrainLine.fromCode("Yellow Line")).isEqualTo(TrainLine.YELLOW);
    }

    @Test
    void fromCode_throwsIllegalArgumentException_whenCodeIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> TrainLine.fromCode("Unknown"));
    }

    @Test
    void getCode_andGetColorHex_returnValues() {
        assertThat(TrainLine.RED.getCode()).isEqualTo("Red");
        assertThat(TrainLine.RED.getColorHex()).isEqualTo("#C60C30");
    }
}
