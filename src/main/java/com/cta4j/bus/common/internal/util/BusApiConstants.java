package com.cta4j.bus.common.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
public final class BusApiConstants {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "ctabustracker.com";

    private static final String API_PREFIX = "/bustime/api/v3";

    public static final String DETOURS_ENDPOINT = "%s/getdetours".formatted(API_PREFIX);
    public static final String DIRECTIONS_ENDPOINT = "%s/getdirections".formatted(API_PREFIX);
    public static final String LOCALES_ENDPOINT = "%s/getlocalelist".formatted(API_PREFIX);
    public static final String PATTERNS_ENDPOINT = "%s/getpatterns".formatted(API_PREFIX);
    public static final String PREDICTIONS_ENDPOINT = "%s/getpredictions".formatted(API_PREFIX);
    public static final String ROUTES_ENDPOINT = "%s/getroutes".formatted(API_PREFIX);
    public static final String STOPS_ENDPOINT = "%s/getstops".formatted(API_PREFIX);
    public static final String SYSTEM_TIME_ENDPOINT = "%s/gettime".formatted(API_PREFIX);
    public static final String VEHICLES_ENDPOINT = "%s/getvehicles".formatted(API_PREFIX);

    private BusApiConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
