package com.cta4j.bus.common.internal.util;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.wire.CtaError;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
    private static final Logger log = LoggerFactory.getLogger(ApiUtils.class);

    public static final int MAX_IDS_PER_REQUEST = 10;

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
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

    public static void checkErrors(@Nullable List<? extends CtaError> errors, String endpoint) {
        Objects.requireNonNull(endpoint);

        if (errors == null || errors.isEmpty()) {
            log.warn("Received empty response from {}", endpoint);

            return;
        }

        boolean notFound = errors.stream()
                                 .allMatch(CtaError::notFound);

        if (notFound) {
            return;
        }

        throw new Cta4jBusException(errors, endpoint);
    }
}
