package com.cta4j.bus.pattern.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaPatternBustimeResponse(
    @Nullable List<CtaPatternError> error,
    @Nullable List<CtaPattern> ptr
) {
    public CtaPatternBustimeResponse {
        if (error != null) {
            error = List.copyOf(error);
        }

        if (ptr != null) {
            ptr = List.copyOf(ptr);
        }
    }
}
