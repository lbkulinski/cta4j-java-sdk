package com.cta4j.bus.api.core.request;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

@NullMarked
public record RequestOptions(
    @Nullable
    Locale locale
) {
    public RequestOptions() {
        this(null);
    }
}
