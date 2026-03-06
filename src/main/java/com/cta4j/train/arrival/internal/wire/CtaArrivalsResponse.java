package com.cta4j.train.arrival.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaArrivalsResponse(
    String tmst,

    int errCd,

    @Nullable
    String errNm,

    List<CtaArrival> eta
) {
    public CtaArrivalsResponse {
        Objects.requireNonNull(tmst);
        Objects.requireNonNull(eta);

        eta = List.copyOf(eta);
    }
}
