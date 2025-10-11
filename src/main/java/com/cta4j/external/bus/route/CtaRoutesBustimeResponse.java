package com.cta4j.external.bus.route;

import java.util.List;

public record CtaRoutesBustimeResponse(
    List<CtaRoute> routes
) {
}
