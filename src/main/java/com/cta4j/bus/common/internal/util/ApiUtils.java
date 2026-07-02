package com.cta4j.bus.common.internal.util;

import com.cta4j.bus.common.internal.wire.CtaError;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "ctabustracker.com";
    public static final String API_PREFIX = "/bustime/api/v3";
    public static final int MAX_IDS_PER_REQUEST = 10;

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String buildErrorMessage(String endpoint, List<? extends CtaError> errors) {
        Objects.requireNonNull(endpoint);
        Objects.requireNonNull(errors);

        errors = List.copyOf(errors);

        String message = errors.stream()
                               .map(CtaError::msg)
                               .reduce("%s; %s"::formatted)
                               .orElse("Unknown error");

        return "Error response from %s: %s".formatted(endpoint, message);
    }

    public static void requireMaxIds(Collection<String> ids, String label) {
        Objects.requireNonNull(ids);
        Objects.requireNonNull(label);

        if (ids.size() > MAX_IDS_PER_REQUEST) {
            String message = "A maximum of %d %s IDs can be requested at once, but %d were provided".formatted(
                MAX_IDS_PER_REQUEST,
                label,
                ids.size()
            );

            throw new IllegalArgumentException(message);
        }
    }
}
