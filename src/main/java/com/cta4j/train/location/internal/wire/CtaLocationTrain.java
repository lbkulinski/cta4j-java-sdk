package com.cta4j.train.location.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaLocationTrain(
    int rn,

    int destSt,

    String destNm,

    int trDr,

    int nextStaId,

    int nextStpId,

    String nextStaNm,

    String prdt,

    String arrT,

    int isApp,

    int isDly,

    @Nullable
    String flags,

    @Nullable
    Double lat,

    @Nullable
    Double lon,

    @Nullable
    Integer heading
) {
    public CtaLocationTrain {
        Objects.requireNonNull(destNm);
        Objects.requireNonNull(nextStaNm);
        Objects.requireNonNull(prdt);
        Objects.requireNonNull(arrT);
        Objects.requireNonNull(lat);
        Objects.requireNonNull(lon);
        Objects.requireNonNull(heading);
    }
}
