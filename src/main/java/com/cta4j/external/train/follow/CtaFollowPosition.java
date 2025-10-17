package com.cta4j.external.train.follow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaFollowPosition(
    String lat,
    String lon,
    String heading
) {
}
