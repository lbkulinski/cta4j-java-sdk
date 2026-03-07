package com.cta4j.train.arrival.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaArrival(
    int staId,

    int stpId,

    String staNm,

    String stpDe,

    int rn,

    String rt,

    int destSt,

    String destNm,

    int trDr,

    String prdt,

    String arrT,

    int isApp,

    int isSch,

    int isDly,

    int isFlt,

    @Nullable
    String flags,

    @Nullable
    Double lat,

    @Nullable
    Double lon,

    @Nullable
    Integer heading
) {
    public CtaArrival {
        Objects.requireNonNull(staNm);
        Objects.requireNonNull(stpDe);
        Objects.requireNonNull(rt);
        Objects.requireNonNull(destNm);
        Objects.requireNonNull(prdt);
        Objects.requireNonNull(arrT);
    }
}
