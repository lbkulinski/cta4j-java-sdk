package com.cta4j.alert.common;

import com.cta4j.alert.common.internal.mapper.Qualifiers;
import org.junit.jupiter.api.Test;

import java.net.URI;

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
}
