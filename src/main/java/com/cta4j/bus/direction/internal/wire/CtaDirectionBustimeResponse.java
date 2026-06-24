package com.cta4j.bus.direction.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaDirectionBustimeResponse(
    @Nullable List<CtaDirectionError> error,
    @Nullable List<CtaDirection> dir
) {
    public CtaDirectionBustimeResponse {
        if (error != null) {
            error = List.copyOf(error);
        }

        if (dir != null) {
            dir = List.copyOf(dir);
        }
    }
}
