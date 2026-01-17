package com.cta4j.bus.api.route;

import com.cta4j.bus.api.route.model.Route;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface RoutesApi {
    List<Route> list();
}
