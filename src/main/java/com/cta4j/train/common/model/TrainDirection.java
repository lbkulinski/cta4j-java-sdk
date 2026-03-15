package com.cta4j.train.common.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the operational direction of a train.
 *
 * <p>
 * <b>NOTE:</b> This direction is operational in nature and does not necessarily reflect the physical direction of the
 * train at its current location. It loosely translates to a northbound or southbound direction, though this may not be
 * intuitive for all lines.
 */
@NullMarked
public enum TrainDirection {
    /**
     * Indicates a northbound operational direction (CTA direction code 1).
     */
    NORTHBOUND(1),

    /**
     * Indicates a southbound operational direction (CTA direction code 5).
     */
    SOUTHBOUND(5);

    /**
     * The CTA direction code associated with this train direction.
     */
    private final int code;

    /**
     * Constructs a {@code TrainDirection}.
     *
     * @param code the CTA direction code associated with this train direction
     * @throws IllegalArgumentException if {@code code} is not 1 (northbound) or 5 (southbound)
     */
    TrainDirection(int code) {
        if ((code != 1) && (code != 5)) {
            throw new IllegalArgumentException("CTA direction code must be either 1 (northbound) or 5 (southbound)");
        }

        this.code = code;
    }

    /**
     * Gets the CTA direction code associated with this direction.
     *
     * @return the CTA direction code
     */
    public int getCode() {
        return this.code;
    }
}
