package com.cta4j.train.common.internal.util;

import com.cta4j.common.exception.Cta4jException;
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
}