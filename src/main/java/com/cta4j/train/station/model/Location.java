package com.cta4j.train.station.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a geographical location with latitude and longitude, optionally including a human-readable address.
 *
 * @param latitude the latitude
 * @param longitude the longitude
 * @param humanAddress the human-readable address, or {@code null} if not available
 */
@NullMarked
public record Location(
    BigDecimal latitude,

    BigDecimal longitude,

    @Nullable
    HumanAddress humanAddress
) {
    /**
     * Constructs a {@code Location}.
     *
     * @param latitude the latitude
     * @param longitude the longitude
     * @param humanAddress the human-readable address, or {@code null} if not available
     * @throws NullPointerException if {@code latitude} or {@code longitude} is {@code null}
     */
    public Location {
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);
    }
}
