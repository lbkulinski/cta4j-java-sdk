package com.cta4j.external.train.follow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaFollowResponse(
    CtaFollowCtatt ctatt
) {
}
