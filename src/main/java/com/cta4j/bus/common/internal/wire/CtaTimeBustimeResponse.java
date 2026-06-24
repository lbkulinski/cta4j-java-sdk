package com.cta4j.bus.common.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaTimeBustimeResponse(
    @Nullable List<CtaTimeError> error,
    @Nullable String tm
) {
    public CtaTimeBustimeResponse {
        if (error != null) {
            error = List.copyOf(error);
        }
    }
}
