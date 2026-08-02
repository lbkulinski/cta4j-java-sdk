package com.cta4j.alert.common;

import com.cta4j.alert.common.internal.mapper.Qualifiers;
import com.cta4j.alert.common.model.ServiceType;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class AlertQualifiersTest {
    @Test
    void mapUri_returnsUri_whenValueIsValid() {
        URI result = Qualifiers.mapUri("http://www.transitchicago.com/redline/");

        assertThat(result).hasToString("http://www.transitchicago.com/redline/");
    }

    @Test
    void mapUri_throwsIllegalArgumentException_whenValueIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.mapUri("not a uri"))
            .withMessageContaining("Failed to parse URI")
            .withCauseInstanceOf(java.net.URISyntaxException.class);
    }

    @Test
    void mapTimestamp_returnsInstant_atChicagoZone_whenTimeOfDayIsPresent() {
        Instant result = Qualifiers.mapTimestamp("2026-07-01T05:00:00");

        // America/Chicago is UTC-5 (CDT) in July.
        assertThat(result).isEqualTo(Instant.parse("2026-07-01T10:00:00Z"));
    }

    @Test
    void mapTimestamp_defaultsToMidnight_whenTimeOfDayIsAbsent() {
        // Confirmed against the live Detailed Alerts API: EventStart/EventEnd are sometimes returned as a
        // bare date (e.g., "2027-09-30") with no time-of-day component.
        Instant result = Qualifiers.mapTimestamp("2027-09-30");

        assertThat(result).isEqualTo(Instant.parse("2027-09-30T05:00:00Z"));
    }

    @Test
    void mapTimestamp_appliesStandardTimeOffset_whenDateIsOutsideDaylightSavingTime() {
        Instant result = Qualifiers.mapTimestamp("2025-11-07");

        assertThat(result).isEqualTo(Instant.parse("2025-11-07T06:00:00Z"));
    }

    @Test
    void mapTimestamp_returnsNull_whenValueIsNull() {
        assertThat(Qualifiers.mapTimestamp(null)).isNull();
    }

    @Test
    void mapTimestamp_throwsIllegalArgumentException_whenValueIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.mapTimestamp("not-a-timestamp"));
    }

    @Test
    void map01ToBoolean_returnsFalse_whenValueIsZero() {
        assertThat(Qualifiers.map01ToBoolean("0")).isFalse();
    }

    @Test
    void map01ToBoolean_returnsTrue_whenValueIsOne() {
        assertThat(Qualifiers.map01ToBoolean("1")).isTrue();
    }

    @Test
    void map01ToBoolean_throwsIllegalArgumentException_whenValueIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.map01ToBoolean("2"));
    }

    @Test
    void mapScore_returnsParsedInt() {
        assertThat(Qualifiers.mapScore("37")).isEqualTo(37);
    }

    @Test
    void mapScore_throwsIllegalArgumentException_whenValueIsNotNumeric() {
        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.mapScore("not-a-number"))
            .withMessageContaining("Failed to parse score: not-a-number")
            .withCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void mapServiceType_returnsCorrectValue_forEachKnownCode() {
        assertThat(Qualifiers.mapServiceType("B")).isEqualTo(ServiceType.BUS);
        assertThat(Qualifiers.mapServiceType("R")).isEqualTo(ServiceType.RAIL);
        assertThat(Qualifiers.mapServiceType("T")).isEqualTo(ServiceType.STATION);
        assertThat(Qualifiers.mapServiceType("X")).isEqualTo(ServiceType.SYSTEMWIDE);
    }

    @Test
    void mapServiceType_throwsIllegalArgumentException_whenCodeIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.mapServiceType("Z"))
            .withMessageContaining("Unknown service type: Z");
    }
}
