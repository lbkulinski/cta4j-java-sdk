package com.cta4j.train.common;

import com.cta4j.common.geo.Coordinates;
import com.cta4j.train.common.internal.mapper.Qualifiers;
import com.cta4j.train.common.internal.wire.CtaArrival;
import com.cta4j.train.common.model.TrainDirection;
import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.follow.internal.wire.CtaPosition;
import com.cta4j.train.station.internal.wire.CtaLocation;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.CardinalDirection;
import com.cta4j.train.station.model.HumanAddress;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class TrainQualifiersTest {
    @Test
    void mapHumanAddress_returnsNull_whenInputIsNull() {
        assertThat(Qualifiers.mapHumanAddress(null)).isNull();
    }

    @Test
    void mapHumanAddress_returnsAddress_whenInputIsValidJson() {
        String json = "{\"address\":\"123 Main St\",\"city\":\"Chicago\",\"state\":\"IL\",\"zip\":\"60601\"}";

        HumanAddress result = Qualifiers.mapHumanAddress(json);

        assertThat(result).isNotNull();
        assertThat(result.address()).isEqualTo("123 Main St");
        assertThat(result.city()).isEqualTo("Chicago");
    }

    @Test
    void mapHumanAddress_throwsIllegalArgumentException_whenInputIsInvalidJson() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            Qualifiers.mapHumanAddress("not-json"));
    }

    @Test
    void map01ToBoolean_throwsIllegalArgumentException_whenValueIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            Qualifiers.map01ToBoolean("2"));
    }

    @Test
    void mapArrivalCoordinates_returnsNull_whenLatIsNull() {
        CtaArrival arrival = new CtaArrival(
            "40100", "30070", "Howard", "Howard (NB)",
            "123", "Red", "30077", "O'Hare", "1",
            "2015-04-30T20:23:53", "2015-04-30T20:25:00",
            "0", "0", "0", "0",
            null, null, null, null
        );

        Coordinates result = Qualifiers.mapArrivalCoordinates(arrival);

        assertThat(result).isNull();
    }

    @Test
    void mapCoordinates_returnsNull_whenPositionLatIsNull() {
        CtaPosition position = new CtaPosition(null, null, null);

        Coordinates result = Qualifiers.mapCoordinates(position);

        assertThat(result).isNull();
    }

    @Test
    void mapCoordinates_returnsNull_whenPositionIsNull() {
        Coordinates result = Qualifiers.mapCoordinates(null);

        assertThat(result).isNull();
    }

    @Test
    void mapArrivalCoordinates_returnsNull_whenLonIsNull() {
        CtaArrival arrival = new CtaArrival(
            "40100", "30070", "Howard", "Howard (NB)",
            "123", "Red", "30077", "O'Hare", "1",
            "2015-04-30T20:23:53", "2015-04-30T20:25:00",
            "0", "0", "0", "0",
            null, "42.019063", null, null
        );

        Coordinates result = Qualifiers.mapArrivalCoordinates(arrival);

        assertThat(result).isNull();
    }

    @Test
    void mapArrivalCoordinates_returnsNull_whenHeadingIsNull() {
        CtaArrival arrival = new CtaArrival(
            "40100", "30070", "Howard", "Howard (NB)",
            "123", "Red", "30077", "O'Hare", "1",
            "2015-04-30T20:23:53", "2015-04-30T20:25:00",
            "0", "0", "0", "0",
            null, "42.019063", "-87.672892", null
        );

        Coordinates result = Qualifiers.mapArrivalCoordinates(arrival);

        assertThat(result).isNull();
    }

    @Test
    void mapTrainLines_includesAllLines_whenStationHasComplementaryLines() {
        CtaLocation location = new CtaLocation("42.019063", "-87.672892", null);
        CtaStation station = new CtaStation(
            "30070", "N", "Test (NB)", "Test", "Test", "40900", false,
            false, true, true, true, false, false, true, true,
            location
        );

        Set<TrainLine> lines = Qualifiers.mapTrainLines(station);

        assertThat(lines).containsExactlyInAnyOrder(
            TrainLine.BLUE, TrainLine.GREEN, TrainLine.BROWN, TrainLine.ORANGE, TrainLine.PINK
        );
        assertThat(lines).doesNotContain(TrainLine.RED, TrainLine.PURPLE, TrainLine.YELLOW);
    }

    @Test
    void mapDirection_returnsNorth_whenDirectionIsN() {
        assertThat(Qualifiers.mapDirection("N")).isEqualTo(CardinalDirection.NORTH);
    }

    @Test
    void mapLine_returnsRed_whenLineIsRed() {
        assertThat(Qualifiers.mapLine("RED")).isEqualTo(TrainLine.RED);
    }

    @Test
    void mapTimestamp_returnsInstant_whenTimestampIsValid() {
        Instant instant = Qualifiers.mapTimestamp("2015-04-30T20:23:53");

        assertThat(instant).isNotNull();
    }

    @Test
    void mapTimestamp_throwsIllegalArgumentException_whenTimestampIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            Qualifiers.mapTimestamp("not-a-timestamp"));
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
    void map15ToTrainDirection_returnsNorthbound_whenDirectionIsOne() {
        assertThat(Qualifiers.map15ToTrainDirection("1")).isEqualTo(TrainDirection.NORTHBOUND);
    }

    @Test
    void map15ToTrainDirection_throwsIllegalArgumentException_whenDirectionIsNotNumeric() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            Qualifiers.map15ToTrainDirection("not-a-number"));
    }

    @Test
    void map15ToTrainDirection_throwsIllegalArgumentException_whenDirectionCodeIsUnrecognized() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            Qualifiers.map15ToTrainDirection("3"));
    }

    @Test
    void parseCoordinate_returnsBigDecimal_whenValueIsValid() {
        assertThat(Qualifiers.parseCoordinate("42.019063")).isEqualByComparingTo(new BigDecimal("42.019063"));
    }

    @Test
    void parseCoordinate_throwsIllegalArgumentException_whenValueIsNotNumeric() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            Qualifiers.parseCoordinate("not-a-number"));
    }

    @Test
    void parseHeading_returnsInt_whenValueIsValid() {
        assertThat(Qualifiers.parseHeading("180")).isEqualTo(180);
    }

    @Test
    void parseHeading_throwsIllegalArgumentException_whenValueIsNotNumeric() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            Qualifiers.parseHeading("not-a-number"));
    }

    @Test
    void mapCoordinates_returnsCoordinates_whenPositionFieldsAreValid() {
        CtaPosition position = new CtaPosition("42.019063", "-87.672892", "180");

        Coordinates result = Qualifiers.mapCoordinates(position);

        assertThat(result).isNotNull();
        assertThat(result.latitude()).isEqualByComparingTo(new BigDecimal("42.019063"));
        assertThat(result.longitude()).isEqualByComparingTo(new BigDecimal("-87.672892"));
        assertThat(result.heading()).isEqualTo(180);
    }

    @Test
    void mapCoordinates_throwsIllegalArgumentException_whenLatIsNotNumeric() {
        CtaPosition position = new CtaPosition("not-a-number", "-87.672892", "180");

        assertThatIllegalArgumentException().isThrownBy(() -> Qualifiers.mapCoordinates(position))
            .withCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void mapArrivalCoordinates_returnsCoordinates_whenAllFieldsPresent() {
        CtaArrival arrival = new CtaArrival(
            "40100", "30070", "Howard", "Howard (NB)",
            "123", "Red", "30077", "O'Hare", "1",
            "2015-04-30T20:23:53", "2015-04-30T20:25:00",
            "0", "0", "0", "0",
            null, "42.019063", "-87.672892", "180"
        );

        Coordinates result = Qualifiers.mapArrivalCoordinates(arrival);

        assertThat(result).isNotNull();
        assertThat(result.heading()).isEqualTo(180);
    }
}
