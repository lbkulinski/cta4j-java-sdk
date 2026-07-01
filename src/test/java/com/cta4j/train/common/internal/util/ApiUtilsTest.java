package com.cta4j.train.common.internal.util;

import com.cta4j.exception.Cta4jException;
import com.cta4j.train.common.internal.wire.CtaError;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ApiUtilsTest {
    @Test
    void parseErrCd_returnsCode_whenErrCdIsValid() {
        int code = ApiUtils.parseErrCd("0", "/api/1.0/ttarrivals.aspx");

        assertThat(code).isZero();
    }

    @Test
    void parseErrCd_throwsCta4jException_whenErrCdIsNotNumeric() {
        assertThatExceptionOfType(Cta4jException.class).isThrownBy(() ->
            ApiUtils.parseErrCd("not-a-number", "/api/1.0/ttarrivals.aspx"));
    }

    @Test
    void parseErrCd_throwsCta4jException_whenErrCdIsNegative() {
        assertThatExceptionOfType(Cta4jException.class).isThrownBy(() ->
            ApiUtils.parseErrCd("-1", "/api/1.0/ttarrivals.aspx"));
    }

    @Test
    void buildErrorMessage_returnsFormattedMessage() {
        CtaError error = new CtaError(500, "Internal error");

        String message = ApiUtils.buildErrorMessage("/api/1.0/ttarrivals.aspx", error);

        assertThat(message).isEqualTo("Error response from /api/1.0/ttarrivals.aspx: [500] Internal error");
    }
}