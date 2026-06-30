package com.cta4j.train.common;

import com.cta4j.common.geo.Coordinates;
import com.cta4j.exception.Cta4jException;
import com.cta4j.train.common.internal.mapper.Qualifiers;
import com.cta4j.train.common.internal.wire.CtaArrival;
import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.follow.internal.wire.CtaPosition;
import com.cta4j.train.station.internal.wire.CtaLocation;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.HumanAddress;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void map01ToBoolean_throwsCta4jException_whenValueIsInvalid() {
        assertThatExceptionOfType(Cta4jException.class).isThrownBy(() ->
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

        List<TrainLine> lines = Qualifiers.mapTrainLines(station);

        assertThat(lines).containsExactlyInAnyOrder(
            TrainLine.BLUE, TrainLine.GREEN, TrainLine.BROWN, TrainLine.ORANGE, TrainLine.PINK
        );
        assertThat(lines).doesNotContain(TrainLine.RED, TrainLine.PURPLE, TrainLine.YELLOW);
    }
}
