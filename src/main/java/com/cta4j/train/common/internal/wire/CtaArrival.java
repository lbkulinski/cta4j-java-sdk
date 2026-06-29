package com.cta4j.train.common.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaArrival(
    String staId,
    String stpId,
    String staNm,
    String stpDe,
    String rn,
    String rt,
    String destSt,
    String destNm,
    String trDr,
    String prdt,
    String arrT,
    String isApp,
    String isSch,
    String isDly,
    String isFlt,
    @Nullable String flags,
    @Nullable String lat,
    @Nullable String lon,
    @Nullable String heading
) {
    public CtaArrival {
        Objects.requireNonNull(staId);
        Objects.requireNonNull(stpId);
        Objects.requireNonNull(staNm);
        Objects.requireNonNull(stpDe);
        Objects.requireNonNull(rn);
        Objects.requireNonNull(rt);
        Objects.requireNonNull(destSt);
        Objects.requireNonNull(destNm);
        Objects.requireNonNull(trDr);
        Objects.requireNonNull(prdt);
        Objects.requireNonNull(arrT);
        Objects.requireNonNull(isApp);
        Objects.requireNonNull(isSch);
        Objects.requireNonNull(isDly);
        Objects.requireNonNull(isFlt);
    }
}
