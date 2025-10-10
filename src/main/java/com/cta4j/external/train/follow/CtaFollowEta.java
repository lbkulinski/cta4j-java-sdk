package com.cta4j.external.train.follow;

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
