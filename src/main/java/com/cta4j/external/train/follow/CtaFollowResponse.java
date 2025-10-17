package com.cta4j.external.train.follow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaFollowResponse(
    CtaFollowCtatt ctatt
) {
}
