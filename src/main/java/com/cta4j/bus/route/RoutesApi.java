package com.cta4j.bus.route;

import com.cta4j.bus.route.model.Route;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface RoutesApi {
    List<Route> list();
}
