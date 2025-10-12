package com.cta4j.mapper.bus;

import com.cta4j.external.bus.route.CtaRoute;
import com.cta4j.model.bus.Route;

import java.util.Objects;

public final class RouteMapper {
    private RouteMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Route fromExternal(CtaRoute route) {
        Objects.requireNonNull(route);

        return new Route(
            route.rt(),
            route.rtnm()
        );
    }
}
