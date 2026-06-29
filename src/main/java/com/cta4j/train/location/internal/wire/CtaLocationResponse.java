package com.cta4j.train.location.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaLocationResponse(
    String tmst,
    String errCd,
    @Nullable String errNm,
    @Nullable List<CtaRoute> route
) {
    public CtaLocationResponse {
        Objects.requireNonNull(tmst);
        Objects.requireNonNull(errCd);

        if (route != null) {
            route = List.copyOf(route);
        }
    }
}
