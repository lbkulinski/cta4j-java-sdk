package com.cta4j.train.common.model;

import org.jspecify.annotations.NullMarked;

/// Represents the operational direction of a train.
///
/// **NOTE:** This direction is operational in nature and does not necessarily reflect the physical direction of the
/// train at its current location. It loosely translates to a northbound or southbound direction, though this may not
/// be intuitive for all lines.
@NullMarked
public enum TrainDirection {
    /// Indicates a northbound operational direction (CTA direction code 1).
    NORTHBOUND(1),

    /// Indicates a southbound operational direction (CTA direction code 5).
    SOUTHBOUND(5);

    private final int code;

    TrainDirection(int code) {
        this.code = code;
    }

    /// Returns the CTA direction code associated with this direction.
    ///
    /// @return the CTA direction code
    public int getCode() {
        return this.code;
    }

    /// Returns the `TrainDirection` corresponding to the given code.
    ///
    /// @param code the CTA direction code (1 for northbound, 5 for southbound)
    /// @return the corresponding `TrainDirection`
    /// @throws IllegalArgumentException if the code does not correspond to any known train direction
    public static TrainDirection fromCode(int code) {
        return switch (code) {
            case 1 -> NORTHBOUND;
            case 5 -> SOUTHBOUND;
            default -> throw new IllegalArgumentException("""
            CTA direction code must be either 1 (northbound) or 5 (southbound)""");
        };
    }
}
