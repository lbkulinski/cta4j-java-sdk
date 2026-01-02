package com.cta4j.bus.mapper;

import com.cta4j.bus.external.route.CtaRoute;
import com.cta4j.bus.model.Route;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class RouteMapper {
    private RouteMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Route fromExternal(CtaRoute route) {
        if (route == null) {
            throw new  IllegalArgumentException("route must not be null");
        }

        return new Route(
            route.rt(),
            route.rtnm()
        );
    }
}
