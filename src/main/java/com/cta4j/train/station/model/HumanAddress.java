package com.cta4j.train.station.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/// Represents a human-readable address.
///
/// @param address the street address
/// @param city the city
/// @param state the state
/// @param zip the ZIP code
@NullMarked
public record HumanAddress(
    String address,
    String city,
    String state,
    String zip
) {
    /// Constructs a `HumanAddress`.
    ///
    /// @param address the street address
    /// @param city the city
    /// @param state the state
    /// @param zip the ZIP code
    /// @throws NullPointerException if `address`, `city`, `state`, or `zip` is `null`
    public HumanAddress {
        Objects.requireNonNull(address);
        Objects.requireNonNull(city);
        Objects.requireNonNull(state);
        Objects.requireNonNull(zip);
    }
}
