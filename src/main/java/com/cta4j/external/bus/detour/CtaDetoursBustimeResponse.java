package com.cta4j.external.bus.detour;

import java.util.List;

public record CtaDetoursBustimeResponse(
    List<CtaDetour> dtrs
) {
}
