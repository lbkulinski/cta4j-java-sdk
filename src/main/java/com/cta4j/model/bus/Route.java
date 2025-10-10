package com.cta4j.model.bus;

import com.cta4j.external.bus.route.CtaRoute;

import java.util.Objects;

public record Route(
    String id,

    String name
) {
    public static Route fromExternal(CtaRoute route) {
        Objects.requireNonNull(route);

        return new Route(
            route.rt(),
            route.rtnm()
        );
    }
}
