package com.cta4j.bus.common;

import com.cta4j.bus.common.internal.mapper.Qualifiers;
import com.cta4j.bus.pattern.model.PatternPointType;
import com.cta4j.bus.prediction.model.PassengerLoad;
import com.cta4j.bus.prediction.model.PredictionType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BusQualifiersTest {
    @Test
    void mapPassengerLoad_returnsFull_whenValueIsFull() {
        assertThat(Qualifiers.mapPassengerLoad("FULL")).isEqualTo(PassengerLoad.FULL);
    }

    @Test
    void mapPassengerLoad_returnsHalfEmpty_whenValueIsHalfEmpty() {
        assertThat(Qualifiers.mapPassengerLoad("HALF_EMPTY")).isEqualTo(PassengerLoad.HALF_EMPTY);
    }

    @Test
    void mapPassengerLoad_throwsIllegalArgumentException_whenValueIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.mapPassengerLoad("PACKED"));
    }

    @Test
    void mapPatternPointType_returnsWaypoint_whenValueIsW() {
        assertThat(Qualifiers.mapPatternPointType("W")).isEqualTo(PatternPointType.WAYPOINT);
    }

    @Test
    void mapPatternPointType_throwsIllegalArgumentException_whenValueIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.mapPatternPointType("X"));
    }

    @Test
    void mapTimestamp_returnsNull_whenTimestampIsNull() {
        assertThat(Qualifiers.mapTimestamp(null)).isNull();
    }

    @Test
    void mapActive_returnsFalse_whenStIsZero() {
        assertThat(Qualifiers.mapActive(0)).isFalse();
    }

    @Test
    void mapPredictionType_returnsDeparture_whenValueIsD() {
        assertThat(Qualifiers.mapPredictionType("D")).isEqualTo(PredictionType.DEPARTURE);
    }

    @Test
    void mapPassengerLoad_returnsUnknown_whenValueIsNA() {
        assertThat(Qualifiers.mapPassengerLoad("N/A")).isEqualTo(PassengerLoad.UNKNOWN);
    }
}
