package com.cta4j.train.common.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
public final class TrainApiConstants {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "lapi.transitchicago.com";
    public static final String DEFAULT_STATIONS_URL = "https://data.cityofchicago.org/resource/8pix-ypme.json";

    private static final String API_PREFIX = "/api/1.0";

    public static final String ARRIVALS_ENDPOINT = "%s/ttarrivals.aspx".formatted(API_PREFIX);
    public static final String FOLLOW_ENDPOINT = "%s/ttfollow.aspx".formatted(API_PREFIX);
    public static final String POSITIONS_ENDPOINT = "%s/ttpositions.aspx".formatted(API_PREFIX);

    private TrainApiConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
