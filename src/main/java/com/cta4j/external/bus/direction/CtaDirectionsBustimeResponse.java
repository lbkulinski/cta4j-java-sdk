package com.cta4j.external.bus.direction;

import java.util.List;

public record CtaDirectionsBustimeResponse(
    List<CtaDirection> directions
) {
}
