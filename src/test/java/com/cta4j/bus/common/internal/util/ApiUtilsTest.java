package com.cta4j.bus.common.internal.util;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.cta4j.bus.route.internal.wire.CtaRouteError;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ApiUtilsTest {
    @Test
    void buildErrorMessage_returnsSingleMessage_whenErrorsHasOneElement() {
        CtaError error = new CtaRouteError("Invalid route", null);

        String message = ApiUtils.buildErrorMessage("/bustime/api/v3/getroutes", List.of(error));

        assertThat(message).isEqualTo("Error response from /bustime/api/v3/getroutes: Invalid route");
    }

    @Test
    void buildErrorMessage_joinsMessages_whenErrorsHasMultipleElements() {
        CtaError first = new CtaRouteError("Invalid route", null);
        CtaError second = new CtaRouteError("Unknown parameter", null);

        String message = ApiUtils.buildErrorMessage("/bustime/api/v3/getroutes", List.of(first, second));

        assertThat(message).isEqualTo(
            "Error response from /bustime/api/v3/getroutes: Invalid route; Unknown parameter"
        );
    }

    @Test
    void buildErrorMessage_returnsUnknownError_whenErrorsIsEmpty() {
        String message = ApiUtils.buildErrorMessage("/bustime/api/v3/getroutes", List.of());

        assertThat(message).isEqualTo("Error response from /bustime/api/v3/getroutes: Unknown error");
    }

    @Test
    void buildErrorMessage_throwsNullPointerException_whenEndpointIsNull() {
        assertThatNullPointerException().isThrownBy(() -> ApiUtils.buildErrorMessage(null, List.of()));
    }

    @Test
    void buildErrorMessage_throwsNullPointerException_whenErrorsIsNull() {
        assertThatNullPointerException().isThrownBy(() -> ApiUtils.buildErrorMessage("/bustime/api/v3/getroutes", null));
    }
}