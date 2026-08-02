package com.cta4j.alert.common.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
public final class AlertApiConstants {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "www.transitchicago.com";

    private static final String API_PREFIX = "/api/1.0";

    public static final String DETAILED_ALERTS_ENDPOINT = "%s/alerts.aspx".formatted(API_PREFIX);
    public static final String ROUTE_STATUS_ENDPOINT = "%s/routes.aspx".formatted(API_PREFIX);

    private AlertApiConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
