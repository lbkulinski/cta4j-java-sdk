package com.cta4j.bus.common.internal.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ApiUtilsTest {
    @Test
    void requireMaxIds_doesNotThrow_whenIdsIsAtMax() {
        List<String> ids = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        assertThatCode(() -> ApiUtils.requireMaxIds(ids, "stop")).doesNotThrowAnyException();
    }

    @Test
    void requireMaxIds_throwsIllegalArgumentException_whenIdsExceedsMax() {
        List<String> ids = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");

        assertThatIllegalArgumentException().isThrownBy(() -> ApiUtils.requireMaxIds(ids, "stop"))
            .withMessage("A maximum of 10 stop IDs can be requested at once, but 11 were provided");
    }

    @Test
    void requireMaxIds_throwsNullPointerException_whenIdsIsNull() {
        assertThatNullPointerException().isThrownBy(() -> ApiUtils.requireMaxIds(null, "stop"));
    }

    @Test
    void requireMaxIds_throwsNullPointerException_whenLabelIsNull() {
        assertThatNullPointerException().isThrownBy(() -> ApiUtils.requireMaxIds(List.of(), null));
    }
}