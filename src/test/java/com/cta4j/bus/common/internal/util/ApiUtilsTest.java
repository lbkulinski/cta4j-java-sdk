package com.cta4j.bus.common.internal.util;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.wire.CtaError;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ApiUtilsTest {
    private record TestError(String msg, boolean notFound) implements CtaError {
    }

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

    @Test
    void checkErrors_doesNotThrow_whenErrorsIsNull() {
        assertThatCode(() -> ApiUtils.checkErrors(null, "/test")).doesNotThrowAnyException();
    }

    @Test
    void checkErrors_doesNotThrow_whenErrorsIsEmpty() {
        assertThatCode(() -> ApiUtils.checkErrors(List.of(), "/test")).doesNotThrowAnyException();
    }

    @Test
    void checkErrors_doesNotThrow_whenAllErrorsAreNotFound() {
        List<CtaError> errors = List.of(new TestError("not found", true));

        assertThatCode(() -> ApiUtils.checkErrors(errors, "/test")).doesNotThrowAnyException();
    }

    @Test
    void checkErrors_throwsCta4jBusException_whenAnyErrorIsNotResourceSpecific() {
        List<CtaError> errors = List.of(new TestError("fatal error", false));

        assertThatThrownBy(() -> ApiUtils.checkErrors(errors, "/test"))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("fatal error")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint()).isEqualTo("/test"));
    }

    @Test
    void checkErrors_throwsNullPointerException_whenEndpointIsNull() {
        assertThatNullPointerException().isThrownBy(() -> ApiUtils.checkErrors(List.of(), null));
    }
}
