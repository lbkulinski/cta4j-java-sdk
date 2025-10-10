package com.cta4j.external.bus.prediction;

import java.util.List;

public record CtaPredictionsBustimeResponse(
    List<CtaPredictionsPrd> prd
) {
}
