package com.cta4j.train.follow.internal.wire;

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
public record CtaFollowResponse(
    String tmst,

    int errCd,

    @Nullable
    String errNm,

    @Nullable
    CtaPosition position,

    List<CtaArrival> eta
) {
    public CtaFollowResponse {
        Objects.requireNonNull(tmst);
        Objects.requireNonNull(eta);

        eta = List.copyOf(eta);
    }
}
