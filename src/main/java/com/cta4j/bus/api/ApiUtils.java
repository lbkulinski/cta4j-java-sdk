package com.cta4j.bus.api;

import com.cta4j.bus.external.CtaError;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public final class ApiUtils {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "ctabustracker.com";
    public static final String API_PREFIX = "/bustime/api/v3";

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String buildErrorMessage(String endpoint, List<CtaError> errors) {
        if (endpoint == null) {
            throw new IllegalArgumentException("endpoint must not be null");
        }

        if (errors == null) {
            throw new IllegalArgumentException("errors must not be null");
        }

        for (CtaError error : errors) {
            if (error == null) {
                throw new IllegalArgumentException("errors must not contain null elements");
            }
        }

        String message = errors.stream()
                               .map(CtaError::msg)
                               .reduce("%s; %s"::formatted)
                               .orElse("Unknown error");

        return String.format("Error response from %s: %s", endpoint, message);
    }
}
