package com.cta4j.common.internal.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.*;

class TimestampParserTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void parse_returnsInstant_atProvidedZone() {
        Instant result = TimestampParser.parse("2026-07-28T12:00:00", FORMATTER, ZoneOffset.UTC);

        assertThat(result).isEqualTo(Instant.parse("2026-07-28T12:00:00Z"));
    }

    @Test
    void parse_appliesZoneId_whenConvertingToInstant() {
        Instant utc = TimestampParser.parse("2026-07-28T12:00:00", FORMATTER, ZoneOffset.UTC);
        Instant chicago = TimestampParser.parse("2026-07-28T12:00:00", FORMATTER, ZoneId.of("America/Chicago"));

        assertThat(chicago).isAfter(utc);
    }

    @Test
    void parse_throwsIllegalArgumentException_whenTimestampDoesNotMatchFormatter() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> TimestampParser.parse("not-a-timestamp", FORMATTER, ZoneOffset.UTC))
            .withMessageContaining("Failed to parse timestamp: not-a-timestamp")
            .withCauseInstanceOf(DateTimeParseException.class);
    }

    @Test
    void parse_throwsNullPointerException_whenTimestampIsNull() {
        assertThatNullPointerException().isThrownBy(() -> TimestampParser.parse(null, FORMATTER, ZoneOffset.UTC));
    }

    @Test
    void parse_throwsNullPointerException_whenFormatterIsNull() {
        assertThatNullPointerException()
            .isThrownBy(() -> TimestampParser.parse("2026-07-28T12:00:00", null, ZoneOffset.UTC));
    }

    @Test
    void parse_throwsNullPointerException_whenZoneIdIsNull() {
        assertThatNullPointerException()
            .isThrownBy(() -> TimestampParser.parse("2026-07-28T12:00:00", FORMATTER, null));
    }

    @Test
    void parseNullable_returnsNull_whenTimestampIsNull() {
        assertThat(TimestampParser.parseNullable(null, FORMATTER, ZoneOffset.UTC)).isNull();
    }

    @Test
    void parseNullable_returnsInstant_whenTimestampIsNonNull() {
        Instant result = TimestampParser.parseNullable("2026-07-28T12:00:00", FORMATTER, ZoneOffset.UTC);

        assertThat(result).isEqualTo(Instant.parse("2026-07-28T12:00:00Z"));
    }

    @Test
    void parseNullable_throwsIllegalArgumentException_whenTimestampDoesNotMatchFormatter() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> TimestampParser.parseNullable("bad", FORMATTER, ZoneOffset.UTC));
    }
}
