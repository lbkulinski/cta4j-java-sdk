package com.cta4j.bus.common.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
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
}
