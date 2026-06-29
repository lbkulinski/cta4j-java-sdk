package com.cta4j.train.common;

import com.cta4j.common.geo.Coordinates;
import com.cta4j.train.common.internal.mapper.Qualifiers;
import com.cta4j.train.common.internal.wire.CtaArrival;
import com.cta4j.train.follow.internal.wire.CtaPosition;
import com.cta4j.train.station.model.HumanAddress;
import org.junit.jupiter.api.Test;

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
}