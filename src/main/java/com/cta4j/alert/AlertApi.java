package com.cta4j.alert;

import com.cta4j.alert.routestatus.RouteStatusApi;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AlertApi {
    RouteStatusApi routeStatus();
}
