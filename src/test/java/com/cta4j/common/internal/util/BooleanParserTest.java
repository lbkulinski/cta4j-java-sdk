package com.cta4j.common.internal.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BooleanParserTest {
    @Test
    void parse01_returnsFalse_whenValueIsZero() {
        assertThat(BooleanParser.parse01("0")).isFalse();
    }

    @Test
    void parse01_returnsTrue_whenValueIsOne() {
        assertThat(BooleanParser.parse01("1")).isTrue();
    }

    @Test
    void parse01_throwsIllegalArgumentException_whenValueIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() -> BooleanParser.parse01("2"))
            .withMessage("Invalid value: 2. Expected 0 or 1");
    }

    @Test
    void parse01_throwsIllegalArgumentException_whenValueIsBlank() {
        assertThatIllegalArgumentException().isThrownBy(() -> BooleanParser.parse01(""));
    }

    @Test
    void parse01_throwsIllegalArgumentException_whenValueIsTrueOrFalseString() {
        assertThatIllegalArgumentException().isThrownBy(() -> BooleanParser.parse01("true"));
    }

    @Test
    void parse01_throwsNullPointerException_whenValueIsNull() {
        assertThatNullPointerException().isThrownBy(() -> BooleanParser.parse01(null));
    }
}