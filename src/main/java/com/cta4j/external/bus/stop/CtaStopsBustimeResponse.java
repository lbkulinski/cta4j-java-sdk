package com.cta4j.external.bus.stop;

import java.util.List;

public record CtaStopsBustimeResponse(
    List<CtaStop> stops
) {
}
