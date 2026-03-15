package com.cta4j.train.arrival.internal.wire;

import com.cta4j.train.common.internal.wire.CtaArrival;
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

    @Nullable
    List<CtaArrival> eta
) {
    public CtaArrivalsResponse {
        Objects.requireNonNull(tmst);

        if (eta != null) {
            eta = List.copyOf(eta);
        }
    }
}
