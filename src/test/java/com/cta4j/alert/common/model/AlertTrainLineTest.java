package com.cta4j.alert.common.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AlertTrainLineTest {
    @Test
    void fromCode_returnsCorrectValue_forEveryDefinedCode() {
        for (AlertTrainLine line : AlertTrainLine.values()) {
            assertThat(AlertTrainLine.fromCode(line.getCode())).isEqualTo(line);
        }
    }

    @Test
    void fromCode_isCaseInsensitive() {
        assertThat(AlertTrainLine.fromCode("red")).isEqualTo(AlertTrainLine.RED);
        assertThat(AlertTrainLine.fromCode("PEXP")).isEqualTo(AlertTrainLine.PURPLE_EXPRESS);
        assertThat(AlertTrainLine.fromCode("pexp")).isEqualTo(AlertTrainLine.PURPLE_EXPRESS);
    }

    @Test
    void fromCode_distinguishesPurpleFromPurpleExpress() {
        assertThat(AlertTrainLine.fromCode("P")).isEqualTo(AlertTrainLine.PURPLE);
        assertThat(AlertTrainLine.fromCode("Pexp")).isEqualTo(AlertTrainLine.PURPLE_EXPRESS);
        assertThat(AlertTrainLine.PURPLE).isNotEqualTo(AlertTrainLine.PURPLE_EXPRESS);
    }

    @Test
    void fromCode_throwsIllegalArgumentException_whenCodeIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> AlertTrainLine.fromCode("Unknown"))
            .withMessageContaining("Invalid alert train line: Unknown");
    }

    @Test
    void fromCode_throwsNullPointerException_whenCodeIsNull() {
        assertThatNullPointerException().isThrownBy(() -> AlertTrainLine.fromCode(null));
    }

    @Test
    void getCode_returnsCode() {
        assertThat(AlertTrainLine.RED.getCode()).isEqualTo("Red");
        assertThat(AlertTrainLine.BROWN.getCode()).isEqualTo("Brn");
        assertThat(AlertTrainLine.GREEN.getCode()).isEqualTo("G");
        assertThat(AlertTrainLine.ORANGE.getCode()).isEqualTo("Org");
        assertThat(AlertTrainLine.PURPLE.getCode()).isEqualTo("P");
        assertThat(AlertTrainLine.PURPLE_EXPRESS.getCode()).isEqualTo("Pexp");
        assertThat(AlertTrainLine.PINK.getCode()).isEqualTo("Pink");
        assertThat(AlertTrainLine.YELLOW.getCode()).isEqualTo("Y");
    }
}