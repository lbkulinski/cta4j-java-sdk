package com.cta4j.train.external.arrival;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaArrivalsCtatt(
    String tmst,
    String errCd,
    String errNm,
    List<CtaArrivalsEta> eta
) {
}
