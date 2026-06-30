package com.cta4j.train.common.internal.util;

import com.cta4j.exception.Cta4jException;
import com.cta4j.train.common.internal.wire.CtaError;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "lapi.transitchicago.com";
    public static final String DEFAULT_STATIONS_URL = "https://data.cityofchicago.org/resource/8pix-ypme.json";
    public static final String API_PREFIX = "/api/1.0";

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int parseErrCd(String errCd, String endpoint) {
        Objects.requireNonNull(errCd);
        Objects.requireNonNull(endpoint);

        try {
            return Integer.parseInt(errCd);
        } catch (NumberFormatException e) {
            String message = "Failed to parse error code from %s".formatted(endpoint);

            throw new Cta4jException(message, e);
        }
    }

    public static String buildErrorMessage(String endpoint, CtaError error) {
        Objects.requireNonNull(endpoint);
        Objects.requireNonNull(error);

        return "Error response from %s: [%d] %s".formatted(endpoint, error.code(), error.message());
    }
}
