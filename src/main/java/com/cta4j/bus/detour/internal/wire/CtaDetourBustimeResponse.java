package com.cta4j.bus.detour.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaDetourBustimeResponse(
    @Nullable List<CtaDetourError> error,
    @Nullable List<CtaDetour> dtrs
) {
    public CtaDetourBustimeResponse {
        if (error != null) {
            error = List.copyOf(error);
        }

        if (dtrs != null) {
            dtrs = List.copyOf(dtrs);
        }
    }
}
