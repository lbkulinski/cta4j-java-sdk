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

    @Nullable
    List<CtaArrival> eta
) {
    public CtaFollowResponse {
        Objects.requireNonNull(tmst);


        if ((errCd != 0) && (errNm == null)) {
            throw new IllegalArgumentException("errNm must be provided when errCd is non-zero");
        }

        if (eta != null) {
            eta = List.copyOf(eta);
        }
    }
}
