package com.cta4j.bus.common;

import com.cta4j.bus.common.internal.mapper.Qualifiers;
import com.cta4j.bus.pattern.model.PatternPointType;
import com.cta4j.bus.prediction.model.DynamicAction;
import com.cta4j.bus.prediction.model.FlagStop;
import com.cta4j.bus.prediction.model.PassengerLoad;
import com.cta4j.bus.prediction.model.PredictionType;
import com.cta4j.bus.vehicle.model.TransitMode;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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
    void mapPredictionType_throwsIllegalArgumentException_whenValueIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.mapPredictionType("X"));
    }

    @Test
    void mapPassengerLoad_returnsUnknown_whenValueIsNA() {
        assertThat(Qualifiers.mapPassengerLoad("N/A")).isEqualTo(PassengerLoad.UNKNOWN);
    }

    @Test
    void mapDynamicAction_returnsNone_whenDynIsZero() {
        assertThat(Qualifiers.mapDynamicAction(0)).isEqualTo(DynamicAction.NONE);
    }

    @Test
    void mapFlagStop_returnsNormal_whenFlagstopIsZero() {
        assertThat(Qualifiers.mapFlagStop(0)).isEqualTo(FlagStop.NORMAL);
    }

    @Test
    void mapTransitMode_returnsBus_whenModeIsOne() {
        assertThat(Qualifiers.mapTransitMode(1)).isEqualTo(TransitMode.BUS);
    }

    @Test
    void mapLocale_returnsLocale_whenLocaleIsEn() {
        assertThat(Qualifiers.mapLocale("en")).isEqualTo(Locale.of("en"));
    }

    @Test
    void mapActive_returnsTrue_whenStIsOne() {
        assertThat(Qualifiers.mapActive(1)).isTrue();
    }

    @Test
    void mapTimestamp_returnsInstant_whenTimestampIsValid() {
        Instant instant = Qualifiers.mapTimestamp("20250101 10:00:00");

        assertThat(instant).isNotNull();
    }

    @Test
    void mapTimestamp_throwsIllegalArgumentException_whenTimestampIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            Qualifiers.mapTimestamp("not-a-timestamp"))
            .withMessage("Failed to parse timestamp: not-a-timestamp")
            .withCauseInstanceOf(DateTimeParseException.class);
    }
}
