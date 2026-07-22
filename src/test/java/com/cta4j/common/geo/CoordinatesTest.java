package com.cta4j.common.geo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class CoordinatesTest {
    @Test
    void constructor_succeeds_whenValuesAreValid() {
        assertThatNoException().isThrownBy(() ->
            new Coordinates(new BigDecimal("41.8827"), new BigDecimal("-87.6233"), 180));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenLatitudeTooLow() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new Coordinates(new BigDecimal("-91"), new BigDecimal("-87.6233"), 180));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenLatitudeTooHigh() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new Coordinates(new BigDecimal("91"), new BigDecimal("-87.6233"), 180));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenLongitudeTooLow() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new Coordinates(new BigDecimal("41.8827"), new BigDecimal("-181"), 180));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenLongitudeTooHigh() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new Coordinates(new BigDecimal("41.8827"), new BigDecimal("181"), 180));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenHeadingTooLow() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new Coordinates(new BigDecimal("41.8827"), new BigDecimal("-87.6233"), -1));
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenHeadingTooHigh() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new Coordinates(new BigDecimal("41.8827"), new BigDecimal("-87.6233"), 360));
    }
}
