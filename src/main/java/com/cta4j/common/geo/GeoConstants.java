package com.cta4j.common.geo;

import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;

@NullMarked
public final class GeoConstants {
    public static final int MIN_HEADING = 0;

    public static final int MAX_HEADING = 359;

    public static final BigDecimal MIN_LATITUDE = BigDecimal.valueOf(-90.0);

    public static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90.0);

    public static final BigDecimal MIN_LONGITUDE = BigDecimal.valueOf(-180.0);

    public static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180.0);

    private GeoConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
