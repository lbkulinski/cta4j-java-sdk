package com.cta4j.alert.detailedalert.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SeverityTest {
    @Test
    void constructor_succeeds_whenScoreIsValid() {
        assertThatNoException().isThrownBy(() -> new Severity(0, "000000", "normal"));
        assertThatNoException().isThrownBy(() -> new Severity(99, "000000", "major"));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenScoreTooLow() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Severity(-1, "000000", "normal"));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenScoreTooHigh() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Severity(100, "000000", "normal"));
    }

    @Test
    void constructor_throwsNullPointerException_whenColorIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new Severity(50, null, "normal"));
    }

    @Test
    void constructor_throwsNullPointerException_whenCssIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new Severity(50, "000000", null));
    }
}
