package com.cta4j.train.common.internal.wire;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CtaErrorTest {
    @Test
    void constructor_setsFields_whenCodeAndMessageAreValid() {
        CtaError error = new CtaError(404, "Not found");

        assertThat(error.code()).isEqualTo(404);
        assertThat(error.message()).isEqualTo("Not found");
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenCodeIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CtaError(-1, "message"));
    }

    @Test
    void constructor_defaultsMessage_whenMessageIsNull() {
        CtaError error = new CtaError(0, null);

        assertThat(error.message()).isEqualTo("An unknown error occurred.");
    }

    @Test
    void constructor_defaultsMessage_whenMessageIsBlank() {
        CtaError error = new CtaError(0, "   ");

        assertThat(error.message()).isEqualTo("An unknown error occurred.");
    }
}