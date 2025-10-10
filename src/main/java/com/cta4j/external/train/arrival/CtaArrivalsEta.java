package com.cta4j.external.train.arrival;

public record CtaArrivalsEta(
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
    String flags,
    String lat,
    String lon,
    String heading
) {
}
