package com.cta4j.train.location.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaLocationTrain(
    String rn,
    String destSt,
    String destNm,
    String trDr,
    String nextStaId,
    String nextStpId,
    String nextStaNm,
    String prdt,
    String arrT,
    String isApp,
    String isDly,
    @Nullable String flags,
    String lat,
    String lon,
    String heading
) {
    public CtaLocationTrain {
        Objects.requireNonNull(rn);
        Objects.requireNonNull(destSt);
        Objects.requireNonNull(destNm);
        Objects.requireNonNull(trDr);
        Objects.requireNonNull(nextStaId);
        Objects.requireNonNull(nextStpId);
        Objects.requireNonNull(nextStaNm);
        Objects.requireNonNull(prdt);
        Objects.requireNonNull(arrT);
        Objects.requireNonNull(isApp);
        Objects.requireNonNull(isDly);
        Objects.requireNonNull(lat);
        Objects.requireNonNull(lon);
        Objects.requireNonNull(heading);
    }
}
