package com.cta4j.train.common.internal.util;

import com.cta4j.common.exception.Cta4jException;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int parseErrCd(String errCd, String endpoint) {
        Objects.requireNonNull(errCd);
        Objects.requireNonNull(endpoint);

        int code;

        try {
            code = Integer.parseInt(errCd);
        } catch (NumberFormatException e) {
            String message = "Failed to parse error code from %s".formatted(endpoint);

            throw new Cta4jException(message, endpoint, e);
        }

        if (code < 0) {
            String message = "Unknown error code %d from %s".formatted(code, endpoint);

            throw new Cta4jException(message, endpoint);
        }

        return code;
    }
}
