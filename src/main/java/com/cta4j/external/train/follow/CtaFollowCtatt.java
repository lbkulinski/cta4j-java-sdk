package com.cta4j.external.train.follow;

import java.util.List;

public record CtaFollowCtatt(
    String tmst,
    String errCd,
    String errNm,
    CtaFollowPosition position,
    List<CtaFollowEta> eta
) {
}
