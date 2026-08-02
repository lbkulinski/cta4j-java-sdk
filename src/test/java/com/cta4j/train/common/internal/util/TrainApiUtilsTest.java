package com.cta4j.train.common.internal.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TrainApiUtilsTest {
    @Test
    void requireMaxIds_doesNotThrow_whenIdsIsAtMax() {
        List<String> ids = List.of("1", "2", "3", "4");

        assertThatCode(() -> TrainApiUtils.requireMaxIds(ids, "map")).doesNotThrowAnyException();
    }

    @Test
    void requireMaxIds_throwsIllegalArgumentException_whenIdsExceedsMax() {
        List<String> ids = List.of("1", "2", "3", "4", "5");

        assertThatIllegalArgumentException().isThrownBy(() -> TrainApiUtils.requireMaxIds(ids, "map"))
            .withMessage("A maximum of 4 map IDs can be requested at once, but 5 were provided");
    }

    @Test
    void requireMaxIds_throwsNullPointerException_whenIdsIsNull() {
        assertThatNullPointerException().isThrownBy(() -> TrainApiUtils.requireMaxIds(null, "map"));
    }

    @Test
    void requireMaxIds_throwsNullPointerException_whenLabelIsNull() {
        assertThatNullPointerException().isThrownBy(() -> TrainApiUtils.requireMaxIds(List.of(), null));
    }
}
