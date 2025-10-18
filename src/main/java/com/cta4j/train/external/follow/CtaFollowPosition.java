package com.cta4j.train.external.follow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaFollowPosition(
    String lat,
    String lon,
    String heading
) {
}
