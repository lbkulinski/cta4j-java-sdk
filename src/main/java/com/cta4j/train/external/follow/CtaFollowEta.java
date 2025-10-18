package com.cta4j.train.external.follow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaFollowEta(
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
    String flags
) {
}
